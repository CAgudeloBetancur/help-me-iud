package co.edu.iudigital.helpmeiud.services.interfaces;

public interface IEmailService {
    boolean sendMail(String mensaje, String email, String asunto);
}
