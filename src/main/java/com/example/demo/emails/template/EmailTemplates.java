package com.example.demo.emails.template;
import com.example.demo.emails.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplates {
    @Autowired
    private EmailService emailService;
    public void sendEmergencyContactNotification(String to, String patientName) {
        String subject = "You Have Been Added as an Emergency Contact";

        String body = """
            <p>Hello,</p>
            <p><strong>%s</strong> has added you as their emergency contact.</p>
            <p>Please log in to view details:</p>
            <a href="http://localhost:4200/auth" 
               style="display:inline-block; padding:10px 18px; background:#2563eb; color:#fff;
               text-decoration:none; border-radius:6px; margin-top:10px;">
               Login
            </a>
            <p>If you did not expect this message, you can ignore it.</p>
            """.formatted(patientName);

        String finalHtml = GlobalTemplate.wrap(body);
        emailService.sendEmail(to, subject, finalHtml);

    }


    public void sendOtpEmail(String to, String otp) {
        String subject = "Your OTP for Verification";

        String body = """
        <p>Hello,</p>
        <p>Your <strong>One-Time Password (OTP)</strong> is:</p>
        
        <h2 style="text-align:center; padding:10px; 
            background:#2563eb; color:#fff; width:120px; 
            border-radius:8px; margin:auto;">
            %s
        </h2>

        <p>This OTP is valid for <strong>5 minutes.</strong></p>
        """.formatted(otp);

        String finalHtml = GlobalTemplate.wrap(body);
        emailService.sendEmail(to, subject, finalHtml);
    }


    public void sendResetLink(String toEmail, String resetLink) {
        String subject = "Password Reset Request";

        String body = """
        <p>Hello,</p>
        <p>We received a request to reset your password.</p>
        <p>Click the link to reset your password:</p>
        <a href="%s"
           style="display:inline-block; padding:10px 20px; 
           background:#dc2626; color:#fff; text-decoration:none; 
           border-radius:6px; margin:15px 0; font-size:16px;">
           Reset Password
        </a>

        """.formatted(resetLink);

        String finalHtml = GlobalTemplate.wrap(body);
        emailService.sendEmail(toEmail, subject, finalHtml);
    }


    public void sendMedicationReminder(String toEmail, String medName, String doseTime, int reminderType) {

        String subject = (reminderType == 1)
                ? "Medication Reminder"
                : "Second Reminder – Medication Not Taken";

        String body;

        if (reminderType == 1) {
            body = """
            <p>Hello,</p>
            <p>This is a reminder to take your medication:</p>

            <p><strong>%s</strong> at <strong>%s</strong></p>

            <p>Please take it on time to maintain your schedule.</p>
            """.formatted(medName, doseTime);
        } else {
            body = """
            <p>Hello,</p>
            <p>You still haven't taken your medication:</p>

            <p><strong>%s</strong></p>

            <p>Please take it as soon as possible for your well-being.</p>
            """.formatted(medName);
        }

        String finalHtml = GlobalTemplate.wrap(body);
        emailService.sendEmail(toEmail, subject, finalHtml);
    }

}
