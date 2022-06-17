package com.nttdata.microservices.account.repository;

import com.nttdata.microservices.account.entity.Account;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface AccountRepository
    extends ReactiveMongoRepository<Account, String>, AccountCustomRepository {

  @Aggregation(pipeline = {"{'$match':{'accountNumber':?0, 'client.documentNumber': ?1}}"})
  Mono<Account> findByAccountNumberAndClientDocument(String accountNumber, String documentNumber);

}
