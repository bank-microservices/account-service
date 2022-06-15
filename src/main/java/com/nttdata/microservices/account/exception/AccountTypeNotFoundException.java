package com.nttdata.microservices.account.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountTypeNotFoundException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public AccountTypeNotFoundException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public AccountTypeNotFoundException(String message) {
    super(message);
  }

  public AccountTypeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }


}
