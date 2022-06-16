package com.nttdata.microservices.account.repository.impl;

import com.nttdata.microservices.account.entity.Account;
import com.nttdata.microservices.account.repository.AccountCustomRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<Account> findByClientIdAndAccountTypeId(String clientId, String accountTypeId) {
    LookupOperation lookup = Aggregation.lookup("account_type", "accountType", "_id", "accountType");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$accountType"), false);
    MatchOperation match = Aggregation.match(Criteria.where("client._id").is(new ObjectId(clientId))
            .and("accountType._id").is(new ObjectId(accountTypeId)));
    Aggregation aggregation = Aggregation.newAggregation(lookup, match, unwindOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Account.class, Account.class);
  }
}
