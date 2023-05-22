package com.frcalderon.stock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleIngredientNotFoundException(Exception e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        CustomException exception = CustomException.builder()
                .message(e.getMessage())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(exception, httpStatus);
    }
}
