package com.nttdata.microservices.account.service.impl;

import com.nttdata.microservices.account.repository.AccountTypeRepository;
import com.nttdata.microservices.account.service.AccountTypeService;
import com.nttdata.microservices.account.service.dto.AccountTypeDto;
import com.nttdata.microservices.account.service.mapper.AccountTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing AccountType.
 */
@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

  private final AccountTypeRepository typeRepository;
  private final AccountTypeMapper typeMapper;

  /**
   * Find all account types and map them to DTOs.
   *
   * @return A Flux of AccountTypeDto objects.
   */
  @Override
  public Flux<AccountTypeDto> findAll() {
    return typeRepository.findAll()
            .map(typeMapper::toDto);
  }

  /**
   * Find an account type by id and map it to a DTO.
   *
   * @param id The id of the account type to be retrieved.
   * @return A Mono of AccountTypeDto
   */
  @Override
  public Mono<AccountTypeDto> findById(String id) {
    return typeRepository.findById(id)
            .map(typeMapper::toDto);
  }

  /**
   * Find an account type by code and map it to a DTO.
   *
   * @param code The code of the account type to find.
   * @return A Mono of AccountTypeDto
   */
  @Override
  public Mono<AccountTypeDto> findByCode(String code) {
    return typeRepository.findByCode(code)
            .map(typeMapper::toDto);
  }

  /**
   * Take the accountTypeDto, map it to an entity, insert it into the repository, and then map it back to
   * a dto.
   *
   * @param accountTypeDto The object that is being passed in from the controller.
   * @return A Mono of AccountTypeDto
   */
  @Override
  public Mono<AccountTypeDto> create(AccountTypeDto accountTypeDto) {
    return Mono.just(accountTypeDto)
            .map(typeMapper::toEntity)
            .flatMap(typeRepository::insert)
            .map(typeMapper::toDto);
  }

  /**
   * Find the account type by id, then map the account type dto to an entity, then set the id of the
   * entity to the id of the account type, then save the entity, then map the entity to a dto.
   *
   * @param id             The id of the account type to update
   * @param accountTypeDto The DTO object that is passed in from the controller.
   * @return A Mono of AccountTypeDto
   */
  @Override
  public Mono<AccountTypeDto> update(String id, AccountTypeDto accountTypeDto) {
    return typeRepository.findById(id)
            .flatMap(p -> Mono.just(accountTypeDto)
                    .map(typeMapper::toEntity)
                    .doOnNext(e -> e.setId(id)))
            .flatMap(typeRepository::save)
            .map(typeMapper::toDto);
  }

  /**
   * It deletes a type by its id
   *
   * @param id The id of the type to delete.
   * @return A Mono of type Void.
   */
  @Override
  public Mono<Void> delete(String id) {
    return typeRepository.deleteById(id);
  }
}
