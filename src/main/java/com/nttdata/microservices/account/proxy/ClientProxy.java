package com.nttdata.microservices.account.proxy;

import com.nttdata.microservices.account.service.dto.ClientDto;
import reactor.core.publisher.Mono;

public interface ClientProxy {

  Mono<ClientDto> getClientByDocumentNumber(String documentNumber);

  Mono<ClientDto> getClientById(String id);

}
