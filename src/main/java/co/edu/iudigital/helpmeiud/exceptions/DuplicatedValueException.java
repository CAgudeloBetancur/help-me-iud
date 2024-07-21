package co.edu.iudigital.helpmeiud.exceptions;

public class DuplicatedValueException extends RestException {

    private static final long serialVersionUID = 1L;

    public DuplicatedValueException() {
        super();
    }

    public DuplicatedValueException(ErrorDto errorDto) {
        super(errorDto);
    }
}
