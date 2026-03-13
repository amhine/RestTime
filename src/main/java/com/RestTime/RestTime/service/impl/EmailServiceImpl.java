package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Réinitialisation de votre mot de passe - RestTime");
        message.setText("Bonjour,\n\n" +
                "Vous avez demandé la réinitialisation de votre mot de passe.\n" +
                "Veuillez cliquer sur le lien ci-dessous pour le modifier :\n\n" +
                resetLink + "\n\n" +
                "Attention, ce lien expirera dans 24 heures.\n" +
                "Si vous n'avez pas fait cette demande, veuillez ignorer cet email.\n\n" +
                "L'équipe RestTime.");

        mailSender.send(message);
    }
}
