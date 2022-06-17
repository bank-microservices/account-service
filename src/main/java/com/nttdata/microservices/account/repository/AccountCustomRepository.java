package com.nttdata.microservices.account.repository;

import com.nttdata.microservices.account.entity.Account;
import com.nttdata.microservices.account.service.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountCustomRepository {

  Mono<AccountDto> findByAccountNumber(String accountNumber);

  Flux<AccountDto> findByClientIdAndAccountTypeId(String clientId, String accountTypeId);

  Flux<AccountDto> findByClientDocument(String documentNumber);

  Mono<Account> updateAccountAmount(Account account);
}
