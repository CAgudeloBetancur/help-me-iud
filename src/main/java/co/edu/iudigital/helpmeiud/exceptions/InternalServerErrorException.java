package co.edu.iudigital.helpmeiud.exceptions;

public class InternalServerErrorException extends RestException {

    private static final long serialVersionUID = 1L;
    private String codigoError;

    public InternalServerErrorException(String message, String codigoError, Exception exception) {
        super(message, exception);
        this.codigoError = codigoError;
    }

    public InternalServerErrorException(String message, Exception exception) {
        super(message, exception);
    }

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(ErrorDto errorDto) {
        super(errorDto);
    }

    public String getCodigoError() {
        return codigoError;
    }
}
