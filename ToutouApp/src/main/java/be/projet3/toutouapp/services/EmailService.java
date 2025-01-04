package be.projet3.toutouapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails.
 * Provides functionality to send simple email messages using the configured {@link JavaMailSender}.
 * Annotated with {@link Service} to indicate it's a Spring service component.
 * @author Sirjacques Célestin
 */
@Service
public class EmailService {


    /**
     * The {@link JavaMailSender} instance used to send emails.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email with the specified recipient, subject, and body.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param body    the body content of the email
     * @author Sirjacques Célestin
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("E-mail envoyé avec succès à " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

