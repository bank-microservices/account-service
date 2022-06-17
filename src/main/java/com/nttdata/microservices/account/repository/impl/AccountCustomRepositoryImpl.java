package com.nttdata.microservices.account.repository.impl;

import com.nttdata.microservices.account.entity.Account;
import com.nttdata.microservices.account.repository.AccountCustomRepository;
import com.nttdata.microservices.account.service.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Mono<AccountDto> findByAccountNumber(String accountNumber) {
    LookupOperation lookup =
        Aggregation.lookup("account_type", "accountType", "_id", "accountType");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$accountType"));
    MatchOperation match = Aggregation.match(Criteria.where("accountNumber").is(accountNumber));
    Aggregation aggregation = Aggregation.newAggregation(lookup, match, unwindOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Account.class, AccountDto.class)
        .singleOrEmpty();
  }

  @Override
  public Flux<AccountDto> findByClientIdAndAccountTypeId(String clientId, String accountTypeId) {
    LookupOperation lookup =
        Aggregation.lookup("account_type", "accountType", "_id", "accountType");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$accountType"), false);
    MatchOperation match = Aggregation.match(Criteria.where("client._id").is(new ObjectId(clientId))
        .and("accountType._id").is(new ObjectId(accountTypeId)));
    Aggregation aggregation = Aggregation.newAggregation(lookup, match, unwindOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Account.class, AccountDto.class);
  }

  @Override
  public Flux<AccountDto> findByClientDocument(String documentNumber) {
    LookupOperation lookup =
        Aggregation.lookup("account_type", "accountType", "_id", "accountType");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$accountType"));
    MatchOperation match =
        Aggregation.match(Criteria.where("client.documentNumber").is(documentNumber));
    Aggregation aggregation = Aggregation.newAggregation(lookup, match, unwindOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Account.class, AccountDto.class);
  }

  @Override
  public Mono<Account> updateAccountAmount(Account account) {
    Query query = new Query(Criteria.where("_id").is(new ObjectId(account.getId())));
    Update update = new Update().set("amount", account.getAmount())
        .set("lastModifiedDate", account.getLastModifiedDate());
    return reactiveMongoTemplate.findAndModify(query, update, Account.class).thenReturn(account);
  }
}
