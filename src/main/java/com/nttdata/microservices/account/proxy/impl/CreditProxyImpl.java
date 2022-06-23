package com.nttdata.microservices.account.proxy.impl;

import com.nttdata.microservices.account.entity.credit.CreditCard;
import com.nttdata.microservices.account.exception.CreditException;
import com.nttdata.microservices.account.proxy.CreditProxy;
import com.nttdata.microservices.account.util.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CreditProxyImpl implements CreditProxy {

  private static final String STATUS_CODE = "Status code : {}";
  private final WebClient webClient;

  public CreditProxyImpl(@Value("${service.credit.uri}") String url,
                         WebClient.Builder loadBalancedWebClientBuilder) {
    this.webClient = loadBalancedWebClientBuilder
        .clientConnector(RestUtils.getDefaultClientConnector())
        .baseUrl(url)
        .build();
  }

  /**
   * Find Credit Card by Client Document Number
   *
   * @param documentNumber Client document
   * @param notFoundMessage Message for error not found
   * @return Credit Card founded
   */
  @Override
  public Mono<CreditCard> findCreditCardByClientDocument(String documentNumber,
                                                         String notFoundMessage) {
    return this.webClient.get()
        .uri("/card/client/{number}", documentNumber)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
            this.applyError4xx(clientResponse, notFoundMessage))
        .onStatus(HttpStatus::is5xxServerError, this::applyError5xx)
        .bodyToFlux(CreditCard.class).singleOrEmpty();
  }

  private Mono<? extends Throwable> applyError4xx(ClientResponse creditResponse,
                                                  String errorMessage) {
    log.info(STATUS_CODE, creditResponse.statusCode().value());
    if (creditResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
      return Mono.error(new CreditException(errorMessage,
          creditResponse.statusCode().value()));
    }
    return creditResponse.bodyToMono(String.class)
        .flatMap(response -> Mono.error(new CreditException(response,
            creditResponse.statusCode().value())));
  }

  private Mono<? extends Throwable> applyError5xx(ClientResponse clientResponse) {
    log.info(STATUS_CODE, clientResponse.statusCode().value());
    return clientResponse.bodyToMono(String.class)
        .flatMap(response -> Mono.error(new CreditException(response)));
  }

}
