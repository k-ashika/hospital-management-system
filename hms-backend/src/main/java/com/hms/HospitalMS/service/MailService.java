package com.hms.HospitalMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        // Skip if mailSender is not configured
        if (mailSender == null || fromEmail.isEmpty()) {
            System.out.println("Email not configured. Would send to: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}