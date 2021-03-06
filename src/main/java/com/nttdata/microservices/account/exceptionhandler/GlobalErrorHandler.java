package com.nttdata.microservices.account.exceptionhandler;

import com.nttdata.microservices.account.exception.BadRequestException;
import com.nttdata.microservices.account.exception.ClientNotFoundException;
import com.nttdata.microservices.account.exception.CreditException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
    log.error("Exception caught in handleRequestBodyError :  {} ", ex.getMessage(), ex);
    var error = ex.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .sorted()
        .collect(Collectors.joining(","));
    log.error("errorList : {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<String> handleClientException(ClientNotFoundException ex) {
    log.error("Exception caught in handleClientException :  {} ", ex.getMessage(), ex);
    log.info("Status value is : {}", ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode())).body(ex.getMessage());
  }

  @ExceptionHandler(CreditException.class)
  public ResponseEntity<String> handleCreditException(CreditException ex) {
    log.error("Exception caught in handleCreditException :  {} ", ex.getMessage(), ex);
    log.info("Status value is : {}", ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode())).body(ex.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
    log.error("Exception caught in handleBadRequestException :  {} ", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

}
