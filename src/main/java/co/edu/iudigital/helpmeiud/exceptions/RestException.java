package co.edu.iudigital.helpmeiud.exceptions;

public class RestException extends Exception {

    private static final long serialVersionUID = 1L;
    private ErrorDto errorDto;

    public RestException() {
        super();
    }

    public RestException(ErrorDto errorDto) {
        super(errorDto.getError());
        this.errorDto = errorDto;
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Exception exception) {
        super(message, exception);
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }
}
