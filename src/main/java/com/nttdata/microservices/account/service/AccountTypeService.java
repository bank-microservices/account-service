package com.nttdata.microservices.account.service;

import com.nttdata.microservices.account.service.dto.AccountTypeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountTypeService {

  Flux<AccountTypeDto> findAll();

  Mono<AccountTypeDto> findById(String id);

  Mono<AccountTypeDto> findByCode(String code);

  Mono<AccountTypeDto> create(AccountTypeDto accountTypeDto);

  Mono<AccountTypeDto> update(String id, AccountTypeDto accountTypeDto);

  Mono<Void> delete(String id);
}
