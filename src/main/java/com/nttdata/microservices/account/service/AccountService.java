package com.nttdata.microservices.account.service;

import com.nttdata.microservices.account.service.dto.AccountDto;
import com.nttdata.microservices.account.service.dto.BalanceDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

  Flux<AccountDto> findAll();

  Mono<AccountDto> findById(String id);

  Mono<AccountDto> findByAccountNumber(String accountNumber);

  Flux<AccountDto> findByClientDocument(String documentNumber);

  Mono<AccountDto> findByAccountNumberAndClientDocument(String accountNumber, String documentNumber);

  Mono<BalanceDto> getBalance(String accountNumber);

  Mono<AccountDto> create(AccountDto accountDto);

  Mono<AccountDto> update(String id, AccountDto accountDto);

  Mono<AccountDto> updateAccountAmount(String accountNumber, Double amount);

  Mono<Void> delete(String id);

}
