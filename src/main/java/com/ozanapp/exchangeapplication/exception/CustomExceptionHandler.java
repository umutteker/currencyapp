package com.ozanapp.exchangeapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {


  @ExceptionHandler(value = {CustomBadRequestException.class})
  public ResponseEntity<Object> handleGeneralBadRequestException(CustomBadRequestException ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());
    body.put("errorCode", ex.getErrorCode());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {CustomNotFoundException.class})
  public ResponseEntity<Object> handleGeneralNotFoundException(CustomNotFoundException ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());
    body.put("errorCode", ex.getErrorCode());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {CustomInternalServerException.class})
  public ResponseEntity<Object> handleGeneralInternalServerException(CustomInternalServerException ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());
    body.put("errorCode", ex.getErrorCode());


    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }


}
