package com.nttdata.microservices.account.controller;

import com.nttdata.microservices.account.service.AccountService;
import com.nttdata.microservices.account.service.dto.AccountDto;
import com.nttdata.microservices.account.service.dto.BalanceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;

  @GetMapping
  public Flux<AccountDto> getAll() {
    log.info("List of Accounts");
    return accountService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<AccountDto>> findById(@PathVariable final String id) {
    log.info("find Account id: {}", id);
    return accountService.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/account-number/{accountNumber}")
  public Mono<ResponseEntity<AccountDto>> findByAccountNumber(@PathVariable String accountNumber) {
    log.info("find Account accountNumber: {}", accountNumber);
    return accountService.findByAccountNumber(accountNumber)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/client-document/{documentNumber}")
  public Flux<AccountDto> findByClientDocument(@PathVariable String documentNumber) {
    log.info("find Account accountNumber: {}", documentNumber);
    return accountService.findByClientDocument(documentNumber);
  }

  @GetMapping("/number/{accountNumber}/client/{documentNumber}")
  public Mono<ResponseEntity<AccountDto>> findByAccountNumberAndClientDocument(@PathVariable String accountNumber,
                                                                               @PathVariable String documentNumber) {
    log.info("find Account accountNumber {} and documentNumber: {}", accountNumber, documentNumber);
    return accountService.findByAccountNumberAndClientDocument(accountNumber, documentNumber)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/balance/{accountNumber}")
  public Mono<ResponseEntity<BalanceDto>> getBalance(@PathVariable String accountNumber) {
    log.info("get Balance Account by accountNumber: {}", accountNumber);
    return accountService.getBalance(accountNumber)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<AccountDto> create(@Valid @RequestBody AccountDto account) {
    return accountService.create(account);
  }

  @PutMapping("/amount/{account-id}/{amount}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<AccountDto> updateAmount(@PathVariable("account-id") String accountId,
                                       @PathVariable Double amount) {
    return accountService.updateAccountAmount(accountId, amount);
  }


  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable String id) {
    log.info("delete Account id: {} ", id);
    return accountService.delete(id);
  }

}
