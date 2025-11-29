package com.example.demo.emails.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendResetLink(String toEmail, String resetLink) {
        String subject = "Password Reset Link";
        String body = "Click the link to reset your password: " + resetLink;
        sendEmail(toEmail, subject, body);
    }

    public void sendOtpEmail(String to, String otp) {
        String subject = "Your OTP for Verification";
        String body = "Your OTP is: " + otp + ". It will expire in 5 minutes.";
        sendEmail(to , subject , body);
    }

    public void sendMedicationReminder(String toEmail, String medName, String doseTime, int reminderType) {

        String subject = (reminderType == 1)
                ? "Medication Reminder"
                : "Second Reminder – Medication Not Taken";

        String body;

        if (reminderType == 1) {
            body = "This is a reminder to take your medication: " + medName +
                    " at " + doseTime + ".";
        } else {
            body = "You still haven't taken your medication: " + medName +
                    ".\nPlease take it as soon as possible.";
        }

        sendEmail(toEmail, subject, body);
    }

}
