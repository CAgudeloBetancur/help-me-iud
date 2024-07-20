package co.edu.iudigital.helpmeiud.services.implementations;

import co.edu.iudigital.helpmeiud.services.interfaces.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImplementation implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean sendMail(String mensaje, String email, String asunto) {
        boolean sent = false;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        try {
            messageHelper.setFrom("noreply@iudigital.edu.co");
            messageHelper.setTo(email);
            messageHelper.setSubject(asunto);
            messageHelper.setText(mensaje);
            mailSender.send(message);
            sent = true;
            log.info("Mensaje enviado exitosamente");
        } catch (MessagingException e) {
            log.error("Error al enviar el mensaje {}", e.getMessage());
        }
        return sent;
    }
}
