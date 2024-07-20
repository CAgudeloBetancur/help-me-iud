package co.edu.iudigital.helpmeiud.exceptions;

public class ForbbidenException extends RestException {

    private static final long serialVersionUID = 1L;

    public ForbbidenException() {
        super();
    }

    public ForbbidenException(ErrorDto errorDto) {
        super(errorDto);
    }

    public ForbbidenException(String message) {
        super(message);
    }
}
