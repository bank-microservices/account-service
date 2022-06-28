package com.nttdata.microservices.account.kafka;

import com.nttdata.microservices.account.service.AccountKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerListener {

  private final AccountKafkaService accountService;

  @KafkaListener(topics = "#{'${kafka.topic.account-request}'}",
      clientIdPrefix = "string", containerFactory = "kafkaListenerStringContainerFactory")
  public void onMessage(ConsumerRecord<String, String> rc) {
    log.info("Consumer record: {}-{}", rc.key(), rc.value());
    accountService.findByNumberAndClientDocument(rc.value(), rc.key()).subscribe();
  }

}
