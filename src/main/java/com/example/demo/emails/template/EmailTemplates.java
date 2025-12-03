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

}
