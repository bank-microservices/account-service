package com.nttdata.microservices.account.repository;

import com.nttdata.microservices.account.entity.AccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface AccountTypeRepository extends ReactiveMongoRepository<AccountType, String> {

  Mono<AccountType> findByCode(String code);

}
