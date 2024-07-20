package co.edu.iudigital.helpmeiud.exceptions;

public class BadRequestException extends RestException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(ErrorDto errorDto) {
        super(errorDto);
    }
}
