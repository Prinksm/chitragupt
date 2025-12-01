package com.example.demo.downloadPrescription;

import com.example.demo.addMedication.repo.*;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.*;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.MedicationsRepo;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DownloadPrescriptionService {

    @Autowired SuperPrescriptionRepo superPrescriptionRepo;
    @Autowired PrescriptionRepo prescriptionRepo;
    @Autowired ConceptRepo conceptRepo;
    @Autowired MedicationStatementRepo medicationStatementRepo;
    @Autowired MedicationsRepo medicationsRepo;
    @Autowired DosageRepo dosageRepo;
    @Autowired TimingRepo timingRepo;

    public byte[] generateSuperPrescriptionPdf(Long superPrescriptionId) {

        SuperPrescription sp = superPrescriptionRepo.findById(superPrescriptionId)
                .orElseThrow(() -> new RuntimeException("SuperPrescription not found"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font sectionTitle = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font label = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font value = new Font(Font.HELVETICA, 12);

            // ---------------- HEADER -------------------
            document.add(new Paragraph("PRESCRIPTION SUMMARY", titleFont));
            document.add(Chunk.NEWLINE);

            // ---------------- PATIENT INFO TABLE -------------------
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell(cell("Prescription ID:", label));
            table.addCell(cell(String.valueOf(sp.getSuperPrescriptionId()), value));

            table.addCell(cell("Patient ID:", label));
            table.addCell(cell(String.valueOf(sp.getPatientId()), value));

            table.addCell(cell("Prescribed By:", label));
            table.addCell(cell("Dr. " + sp.getDoctorName(), value));

            table.addCell(cell("Date Issued:", label));
            table.addCell(cell(formatDate(sp.getPrescriptionDate().toString()), value));

            if (sp.getNotes() != null && !sp.getNotes().isBlank()) {
                table.addCell(cell("Doctor Notes:", label));
                table.addCell(cell(sp.getNotes(), value));
            }

            document.add(table);
            document.add(lineBreak());
            document.add(separator());

            // ---------------- PRESCRIPTIONS -------------------
            List<Prescription> prescriptions =
                    prescriptionRepo.findBySuperPrescriptionId(superPrescriptionId);

            for (Prescription p : prescriptions) {

                Concepts condition = conceptRepo.findById(p.getReasonId()).orElse(null);
                String conditionName = condition != null ? condition.getConceptName() : "General Treatment";

                document.add(new Paragraph("Medication Course", sectionTitle));
                document.add(new Paragraph("Condition: " + conditionName, value));

                if (p.getNotes() != null && !p.getNotes().isBlank()) {
                    document.add(new Paragraph("Course Notes: " + p.getNotes(), value));
                }
                document.add(Chunk.NEWLINE);

                List<MedicationStatements> statements =
                        medicationStatementRepo.findByPrescriptionId(p.getPrescriptionId());

                int index = 1;

                for (MedicationStatements ms : statements) {

                    Medications medicine = medicationsRepo.findById(ms.getMedicationId()).orElse(null);
                    String medName = medicine != null ? medicine.getBrandName() : "Medication";

                    // Medication title
                    document.add(new Paragraph(index++ + ") " + medName,
                            new Font(Font.HELVETICA, 13, Font.BOLD)));
                    document.add(Chunk.NEWLINE);

                    // Medication details table
                    PdfPTable medTable = new PdfPTable(2);
                    medTable.setWidthPercentage(100);

                    medTable.addCell(cell("Status:", label));
                    medTable.addCell(cell(ms.getStatus(), value));

                    if (ms.getEffectiveStartDate() != null) {
                        medTable.addCell(cell("Start Date:", label));
                        medTable.addCell(cell(formatDate(ms.getEffectiveStartDate().toString()), value));
                    }

                    if (ms.getEffectiveEndDate() != null) {
                        medTable.addCell(cell("End Date:", label));
                        medTable.addCell(cell(formatDate(ms.getEffectiveEndDate().toString()), value));
                    }

                    document.add(medTable);
                    document.add(Chunk.NEWLINE);

                    // ---------------- DOSAGE ----------------
                    Dosages d = null;
                    if (ms.getDosageId() != null) {
                        d = dosageRepo.findById(ms.getDosageId()).orElse(null);
                    }

                    if (d != null) {
                        Concepts amountUnit = conceptRepo.findById(d.getAmountUnitId()).orElse(null);
                        Concepts route = conceptRepo.findById(d.getRouteId()).orElse(null);

                        document.add(new Paragraph("Dosage:", label));

                        if (d.getAmount() != null)
                            document.add(new Paragraph("• Take: " +  cleanDecimal(d.getAmount())+ " " +
                                    (amountUnit != null ? amountUnit.getConceptName() : ""), value));

                        if (route != null)
                            document.add(new Paragraph("• Route: " + route.getConceptName(), value));

                        if (d.getInstruction() != null && !d.getInstruction().isBlank())
                            document.add(new Paragraph("• Additional Instructions: " + d.getInstruction(), value));

                        document.add(Chunk.NEWLINE);
                    }

                    // ---------------- TIMING ----------------
                    if (d != null && d.getTimingId() != null) {
                        Timing t = timingRepo.findById(d.getTimingId()).orElse(null);

                        if (t != null) {
                            Concepts periodUnit = conceptRepo.findById(t.getPeriodUnitId()).orElse(null);
                            Concepts whenCode = conceptRepo.findById(t.getWhenCodeId()).orElse(null);

                            document.add(new Paragraph("Instructions:", label));

                            String readableTiming = buildReadableTiming(t, periodUnit, whenCode);

                            document.add(new Paragraph("• " + readableTiming, value));
                        }
                    }

                    document.add(lineBreak());
                    document.add(separator());
                }
            }

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return baos.toByteArray();
    }

    // ----------------- HELPER FUNCTIONS -------------------

    private PdfPCell cell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6);
        return cell;
    }

    private Paragraph lineBreak() {
        return new Paragraph(" ");
    }

    private LineSeparator separator() {
        LineSeparator ls = new LineSeparator();
        ls.setLineColor(Color.GRAY);
        return ls;
    }

    private String formatDate(String raw) {
        try {
            return raw.substring(0, 10);
        } catch (Exception e) {
            return raw;
        }
    }
    private String cleanDecimal(Number number) {
        if (number == null) return "";

        double value = number.doubleValue();

        // If whole number ⇒ show int
        if (value == Math.floor(value)) {
            return String.valueOf((int) value);
        }

        // Otherwise show maximum 2 decimal places
        return String.format("%.2f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private String buildReadableTiming(Timing t, Concepts periodUnit, Concepts whenCode) {

        String frequency = t.getFrequency() != null ? t.getFrequency().toString() : null;
        String period = t.getPeriod() != null ? t.getPeriod().toString() : null;
        String unit = periodUnit != null ? periodUnit.getConceptName() : "";
        String when = whenCode != null ? whenCode.getConceptName() : "";

        String timeOfDay = "";
        if (t.getTimeOfDay() != null) {
            try {
                LocalTime time = t.getTimeOfDay().toLocalTime();
                timeOfDay = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
            } catch (Exception e) {
                timeOfDay = t.getTimeOfDay().toString();
            }
        }

        StringBuilder readable = new StringBuilder();

        if (frequency != null && period != null) {
            readable.append("Take ").append(frequency)
                    .append(" time(s) every ")
                    .append(period).append(" ").append(unit);
        }

        if (!when.isEmpty())
            readable.append(" (").append(when).append(")");

        if (!timeOfDay.isEmpty())
            readable.append(" — Recommended time: ").append(timeOfDay);

        if (readable.isEmpty())
            readable.append("Follow doctor’s timing instructions.");

        return readable.toString();
    }
}
