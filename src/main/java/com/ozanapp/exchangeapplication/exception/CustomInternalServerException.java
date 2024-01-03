package com.ozanapp.exchangeapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomInternalServerException extends Exception{
  private final String errorCode;
  public CustomInternalServerException(String message, String code) {

    super(message);
    this.errorCode = code;
  }

  public CustomInternalServerException(String message, Throwable cause, String errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
