package com.example.demo.emails.services;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// // import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    // private JavaMailSender mailSender;
    private final ResendEmailClient resendEmailClient;

    // public EmailService(JavaMailSender mailSender) {
    //     this.mailSender = mailSender;
    // }

    // public void sendEmail(String to, String subject, String body) {
        // try {
        //     MimeMessage message = mailSender.createMimeMessage();
        //     MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        //     helper.setTo(to);
        //     helper.setSubject(subject);
        //     helper.setText(body, true); // true → HTML email

        //     mailSender.send(message);

        // } catch (MessagingException e) {
        //     throw new RuntimeException("Failed to send email", e);
        // }
            // mailSender.send(message);
    // }

  public void sendEmail(String to, String subject, String body) {
        // body is HTML already
        resendEmailClient.send(to, subject, body);
    }

}
