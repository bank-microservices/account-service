package com.nttdata.microservices.account.repository;

import com.nttdata.microservices.account.entity.Account;
import reactor.core.publisher.Flux;

public interface AccountCustomRepository {

  Flux<Account> findByClientIdAndAccountTypeId(String clientId, String accountTypeId);

}
