package com.nttdata.microservices.account.service;

import com.nttdata.microservices.account.service.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

  Flux<AccountDto> findAll();

  Mono<AccountDto> findById(String id);

  Mono<AccountDto> findByAccountNumber(String accountNumber);

  Mono<AccountDto> findByAccountNumberAndClientDocumentNumber(String accountNumber, String documentNumber);

  Mono<AccountDto> create(AccountDto accountDto);

  Mono<AccountDto> update(String id, AccountDto accountDto);

  Mono<Void> delete(String id);

  Mono<AccountDto> updateAccountAmount(String accountNumber, Double amount);
}
