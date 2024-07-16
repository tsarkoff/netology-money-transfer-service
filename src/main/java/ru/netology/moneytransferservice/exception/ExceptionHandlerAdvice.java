package ru.netology.moneytransferservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return new ResponseEntity<>(
                "IllegalArgumentException in Service method.\n Exception message: "
                        + e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> internalErrorHandler(InternalError e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(
                "InternalError in Service method.\n Exception message: "
                        + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolationHandler(ConstraintViolationException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(
                "ConstraintViolation Exception in Service method.\n Exception message: "
                        + e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
