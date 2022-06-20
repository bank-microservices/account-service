package com.nttdata.microservices.account.proxy;


import com.nttdata.microservices.account.entity.credit.CreditCard;
import reactor.core.publisher.Mono;

public interface CreditProxy {

  Mono<CreditCard> findCreditCardByClientDocument(String documentNumber,
                                                  String notFoundMessage);
}
