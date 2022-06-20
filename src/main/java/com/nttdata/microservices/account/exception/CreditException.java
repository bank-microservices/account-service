package com.nttdata.microservices.account.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreditException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public CreditException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public CreditException(String message) {
    super(message);
  }

  public CreditException(String message, Throwable cause) {
    super(message, cause);
  }
}
