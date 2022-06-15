package com.nttdata.microservices.account.controller;

import com.nttdata.microservices.account.service.AccountTypeService;
import com.nttdata.microservices.account.service.dto.AccountTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/account/type")
@RequiredArgsConstructor
public class AccountTypeController {

  private final AccountTypeService typeService;

  @GetMapping
  public Flux<AccountTypeDto> getAll() {
    log.info("List of Account Type");
    return typeService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<AccountTypeDto>> findById(@PathVariable String id) {
    log.info("get Account Type id: {}", id);
    return typeService.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<AccountTypeDto> create(@Valid @RequestBody AccountTypeDto typeDto) {
    log.info("create Account Type : {}", typeDto);
    return typeService.create(typeDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<AccountTypeDto>> update(@PathVariable String id, @Valid @RequestBody AccountTypeDto typeDto) {
    log.info("update Account Type id: {} - values : {}", id, typeDto);
    return typeService.update(id, typeDto)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable String id) {
    log.info("delete Account Type id: {} ", id);
    return typeService.delete(id);
  }

}
