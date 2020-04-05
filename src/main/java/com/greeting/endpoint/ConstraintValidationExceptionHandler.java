package com.greeting.endpoint;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Arrays;

import static com.greeting.util.Constants.ENUM_ERROR_VALIDATION_MESSAGE;
import static com.greeting.util.Constants.NUMBER_FORMAT_VIOLATION_MESSAGE;

@ControllerAdvice
public class ConstraintValidationExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    void constraintViolationHandler(NumberFormatException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format(NUMBER_FORMAT_VIOLATION_MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    void constraintViolationHandler(ConstraintViolationException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConversionFailedException.class)
    void handleConversionFailedException(ConversionFailedException e, HttpServletResponse response) throws IOException {
        String message = e.getTargetType().getType().isEnum() ?
                String.format(ENUM_ERROR_VALIDATION_MESSAGE, e.getValue(), Arrays.toString(e.getTargetType().getType().getEnumConstants()))
                : e.getMessage();

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

}