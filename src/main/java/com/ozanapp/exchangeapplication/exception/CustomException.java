package com.ozanapp.exchangeapplication.exception;

import org.springframework.http.HttpStatus;

public class CustomException {
  private final String errrorCode;
  private final String message;
  private final HttpStatus httpStatus;

  public CustomException(String errrorCode, String message, HttpStatus httpStatus) {
    this.errrorCode = errrorCode;
    this.message = message;
    this.httpStatus = httpStatus;
  }
  public String getErrorCode() {
    return errrorCode;
  }
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
