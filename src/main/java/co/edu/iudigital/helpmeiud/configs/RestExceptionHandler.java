package co.edu.iudigital.helpmeiud.configs;

import co.edu.iudigital.helpmeiud.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> getGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorDto errorRq = ErrorDto.getErrorDto(
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorRq, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ErrorDto> getGeneralException(InternalServerErrorException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> getNotFoundRequest(NotFoundException exception) {
        log.info(exception.getMessage());
        return new ResponseEntity<>(exception.getErrorDto(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorDto> getBadRequestException(BadRequestException exception) {
        log.info(exception.getErrorDto().getMessage());
        return new ResponseEntity<>(exception.getErrorDto(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DuplicatedValueException.class})
    public ResponseEntity<ErrorDto> getConflictException(DuplicatedValueException exception) {
        log.info(exception.getErrorDto().getMessage());
        return new ResponseEntity<>(exception.getErrorDto(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorDto> getUnauthorizedException(UnauthorizedException exception) {
        log.info(exception.getErrorDto().getMessage());
        return new ResponseEntity<>(exception.getErrorDto(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbbidenException.class})
    public ResponseEntity<ErrorDto> getUnauthorizedException(ForbbidenException e) {
        log.info(e.getErrorDto().getMessage());
        return new ResponseEntity<>(e.getErrorDto(), HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        if(fieldErrors != null & !fieldErrors.isEmpty()) {
            errorMessage.append(fieldErrors.get(0).getDefaultMessage());
        } else {
            errorMessage.append("Ocurrio un error al procesar la solicitud. Por favor intente de nuevo");
        }
        ErrorDto errorInfo = ErrorDto.getErrorDto(
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            errorMessage.toString(),
            HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
