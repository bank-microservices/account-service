package com.nttdata.microservices.account.repository;

import com.nttdata.microservices.account.entity.Account;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository
    extends ReactiveMongoRepository<Account, String>, AccountCustomRepository {

  @Override
  @Aggregation(pipeline = {
      "{$lookup: { from: 'account_type', localField: 'accountType', foreignField: '_id', as: 'accountType'}}",
      "{$unwind: {path: '$accountType', preserveNullAndEmptyArrays: false }}"
  })
  Flux<Account> findAll();

  @Aggregation(pipeline = {"{'$match':{'accountNumber':?0, 'client.documentNumber': ?1}}"})
  Mono<Account> findByAccountNumberAndClientDocument(String accountNumber, String documentNumber);

}
