package com.ozanapp.exchangeapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomBadRequestException extends Exception {
  private final String errorCode;

  public CustomBadRequestException(String message, String errorCode) {

    super(message);
    this.errorCode = errorCode;
  }

  public CustomBadRequestException(String message, Throwable cause, String code) {
    super(message, cause);
    this.errorCode = code;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
