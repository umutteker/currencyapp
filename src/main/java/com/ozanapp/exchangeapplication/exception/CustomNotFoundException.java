package com.ozanapp.exchangeapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends Exception{

  private final String errorCode;
  public CustomNotFoundException(String message,String code) {
    super(message);
    this.errorCode = code;
  }

  public CustomNotFoundException(String message, Throwable cause, String code) {

    super(message, cause);
    this.errorCode = code;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
