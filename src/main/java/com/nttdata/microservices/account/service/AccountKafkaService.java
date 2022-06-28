package com.nttdata.microservices.account.service;

import reactor.core.publisher.Mono;

public interface AccountKafkaService {

  Mono<Void> findByNumberAndClientDocument(String accountNumber, String documentNumber);

}
