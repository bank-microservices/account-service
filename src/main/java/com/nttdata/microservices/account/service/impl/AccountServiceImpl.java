package com.nttdata.microservices.account.service.impl;

import static com.nttdata.microservices.account.util.MessageUtils.getMsg;

import com.nttdata.microservices.account.entity.client.ClientType;
import com.nttdata.microservices.account.exception.AccountTypeNotFoundException;
import com.nttdata.microservices.account.exception.BadRequestException;
import com.nttdata.microservices.account.exception.ClientNotFoundException;
import com.nttdata.microservices.account.proxy.ClientProxy;
import com.nttdata.microservices.account.repository.AccountRepository;
import com.nttdata.microservices.account.repository.AccountTypeRepository;
import com.nttdata.microservices.account.service.AccountService;
import com.nttdata.microservices.account.service.dto.AccountDto;
import com.nttdata.microservices.account.service.dto.enums.EAccountType;
import com.nttdata.microservices.account.service.mapper.AccountMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

/**
 * Service Implementation for managing Account.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final AccountTypeRepository typeRepository;

  private final AccountMapper accountMapper;

  private final ClientProxy clientProxy;

  /**
   * Find all accounts and map them to a DTO
   *
   * @return A Flux of AccountDto objects.
   */
  @Override
  public Flux<AccountDto> findAll() {
    return accountRepository.findAll().log()
        .map(accountMapper::toDto);
  }

  /**
   * Find an account by id and return a Mono of AccontDto
   *
   * @param id The id of the account to be retrieved.
   * @return A Mono of AccountDto
   */
  @Override
  public Mono<AccountDto> findById(String id) {
    return accountRepository.findById(id)
        .map(accountMapper::toDto);
  }

  /**
   * It takes a String accountNumber, finds the account in the database, and
   * returns a Mono of AccountDto
   *
   * @param accountNumber The account number of the account to be retrieved.
   * @return A Mono of AccountDto
   */
  @Override
  public Mono<AccountDto> findByAccountNumber(String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  @Override
  public Flux<AccountDto> findByClientDocument(String documentNumber) {
    return accountRepository.findByClientDocument(documentNumber);
  }

  /**
   * Find an account by account number and client document number, and if found,
   * map it to an account DTO.
   *
   * @param accountNumber  number of the Account
   * @param documentNumber document number of client
   * @return A Mono&lt;AccountDto&gt;
   */
  @Override
  public Mono<AccountDto> findByAccountNumberAndClientDocument(String accountNumber,
                                                               String documentNumber) {
    return accountRepository.findByAccountNumberAndClientDocument(accountNumber, documentNumber)
        .map(accountMapper::toDto);
  }

  /**
   * Create a new account, if the client exists, if the account number does not
   * exist, if the account type exists, if the client type is valid for the
   * account type, if the maintenance fee is valid, if the maximum limit of movements is valid, if the
   * day allowed is valid, then map the accountDto to an entity, set the createAt and status, insert the
   * account, and map the account to a Dto.
   *
   * @param accountDto It's a DTO that contains the data to be saved in the
   *                   database.
   * @return A Mono of AccountDto
   */
  @Override
  public Mono<AccountDto> create(AccountDto accountDto) {
    return Mono.just(accountDto)
        .flatMap(this::existClient)
        .flatMap(this::existAccountNumber)
        .flatMap(this::existAccountType)
        .flatMap(this::validateClientTypeByAccountType)
        .flatMap(this::validateMaintenanceFee)
        .flatMap(this::validateMaxLimitMovement)
        .flatMap(this::validateDayAllowed)
        .map(accountMapper::toEntity)
        .map(account -> {
          account.setCreateAt(LocalDateTime.now());
          account.setStatus(true);
          return account;
        })
        .flatMap(this.accountRepository::insert)
        .map(accountMapper::toDto)
        .subscribeOn(Schedulers.boundedElastic());
  }

  /**
   * If the client is not found, throw an exception, otherwise, set the client and
   * return the accountDto
   *
   * @param accountDto is the object that I'm passing to the method
   * @return A Mono of AccountDto
   */
  private Mono<AccountDto> existClient(AccountDto accountDto) {

    log.debug("Request to proxy Client by documentNumber: {}",
        accountDto.getClientDocumentNumber());
    return clientProxy.getClientByDocumentNumber(accountDto.getClientDocumentNumber())
        .switchIfEmpty(Mono.error(new ClientNotFoundException(getMsg("client.not.found"))))
        .doOnNext(accountDto::setClient)
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
        .thenReturn(accountDto);
  }

  /**
   * If the account number already exists, throw a BadRequestException, otherwise
   * return the accountDto
   *
   * @param accountDto The object that will be validated.
   * @return The accountDto object is being returned.
   */
  private Mono<AccountDto> existAccountNumber(AccountDto accountDto) {
    return findByAccountNumber(accountDto.getAccountNumber())
        .flatMap(r -> Mono.error(new BadRequestException(getMsg("account.already"))))
        .thenReturn(accountDto);
  }

  /**
   * If the account type is not found, throw an exception. Otherwise, set the
   * account type and return the
   * account dto
   *
   * @param accountDto The object that will be returned by the method.
   * @return The return type is Mono of AccountDto
   */
  private Mono<AccountDto> existAccountType(AccountDto accountDto) {
    return typeRepository.findByCode(accountDto.getAccountTypeCode())
        .switchIfEmpty(
            Mono.error(new AccountTypeNotFoundException(getMsg("account.type.not.found"))))
        .doOnNext(accountDto::setAccountType)
        .thenReturn(accountDto);
  }

  /**
   * It validates that a client of type PERSONAL can only have one account of a
   * given type, and that a
   * client of type BUSINESS can only have one account of type CURRENT, and that
   * the account must have at
   * least one owner
   *
   * @param accountDto AccountDto
   * @return A Mono of AccountDto
   */
  private Mono<AccountDto> validateClientTypeByAccountType(AccountDto accountDto) {
    return Mono.just(accountDto)
        .flatMap(dto -> accountRepository
            .findByClientIdAndAccountTypeId(dto.getClient().getId(), dto.getAccountType().getId())
            .count()
            .handle((count, sink) -> {
              if (ClientType.PERSONAL == dto.getClient().getClientType() && count > 0) {
                sink.error(new BadRequestException(getMsg("account.client.already.registered.type",
                    dto.getClient().getFirstNameBusiness(),
                    dto.getClient().getClientType().name(),
                    dto.getAccountType().getDescription())));
              } else {
                sink.complete();
              }
            })
            .thenReturn(dto))
        .<AccountDto>handle((dto, sink) -> {
          if (ClientType.BUSINESS == dto.getClient().getClientType()) {
            if ((EAccountType.SAVING.equalValue(accountDto.getAccountType().getCode())
                || EAccountType.FIXED_TERM.equalValue(accountDto.getAccountType().getCode()))) {
              sink.error(new BadRequestException(getMsg("account.client.cannot.registered.type",
                  dto.getClient().getFirstNameBusiness(),
                  dto.getClient().getClientType().name(),
                  dto.getAccountType().getDescription())));
            } else if (CollectionUtils.isEmpty(accountDto.getOwners())) {
              sink.error(new BadRequestException(getMsg("account.owner.required",
                  dto.getClient().getClientType().name())));
            } else if (accountDto.getOwners().stream()
                .anyMatch(h -> h.getDocumentNumber().equals(dto.getClient().getDocumentNumber()))) {
              sink.error(new BadRequestException(getMsg("account.owner.document.equals")));
            } else {
              sink.complete();
            }
          } else {
            sink.complete();
          }
        })
        .thenReturn(accountDto);
  }

  /**
   * If the account type is a savings or fixed term account, then the maintenance
   * fee should be zero
   *
   * @param accountDto The object that is being validated
   * @return The return type is Mono of AccountDto
   */
  private Mono<AccountDto> validateMaintenanceFee(AccountDto accountDto) {
    return Mono.just(accountDto)
        .<AccountDto>handle((dto, sink) -> {
          if ((EAccountType.SAVING.equalValue(dto.getAccountType().getCode())
              || EAccountType.FIXED_TERM.equalValue(dto.getAccountType().getCode()))
              && dto.getMaintenanceFee() > 0) {
            sink.error(new BadRequestException(getMsg("account.maintenance.not.have",
                dto.getAccountType().getDescription())));
          } else if (EAccountType.CURRENT.equalValue(dto.getAccountType().getCode())
              && dto.getMaintenanceFee() <= 0) {
            sink.error(new BadRequestException(getMsg("account.maintenance.required.have",
                dto.getAccountType().getDescription())));
          } else {
            sink.complete();
          }
        })
        .thenReturn(accountDto);
  }

  /**
   * It validates the maximum number of movements for an account type
   *
   * @param accountDto The object that will be validated
   * @return A Mono&lt;AccountDto&gt;
   */
  private Mono<AccountDto> validateMaxLimitMovement(AccountDto accountDto) {
    return Mono.just(accountDto)
        .<AccountDto>handle((dto, sink) -> {
          Integer maxLimit = dto.getMaxLimitMonthlyMovements();
          if (EAccountType.SAVING.equalValue(dto.getAccountType().getCode()) && maxLimit <= 0) {
            sink.error(new BadRequestException(getMsg("account.movements.maximum",
                dto.getAccountType().getDescription())));
          } else if (EAccountType.CURRENT.equalValue(dto.getAccountType().getCode())
              && maxLimit != 0) {
            sink.error(new BadRequestException(getMsg("account.movements.not.maximum",
                dto.getAccountType().getDescription())));
          } else if (EAccountType.FIXED_TERM.equalValue(dto.getAccountType().getCode())
              && maxLimit != 1) {
            sink.error(new BadRequestException(getMsg("account.movements.maximum.one",
                dto.getAccountType().getDescription())));
          } else {
            sink.complete();
          }
        })
        .thenReturn(accountDto);
  }

  /**
   * It validates the day of the month that a movement is allowed to be made on a
   * given account
   *
   * @param accountDto The object that is being validated
   * @return The return type is Mono of AccountDto
   */
  private Mono<AccountDto> validateDayAllowed(AccountDto accountDto) {
    return Mono.just(accountDto)
        .<AccountDto>handle((dto, sink) -> {
          Integer dayAllowed = dto.getDayAllowed();
          if (EAccountType.SAVING.equalValue(dto.getAccountType().getCode())
              || EAccountType.CURRENT.equalValue(dto.getAccountType().getCode())) {
            if (dayAllowed != 0) {
              sink.error(new BadRequestException(getMsg("account.day.allowed.not",
                  dto.getAccountType().getDescription())));
            }
          } else if (EAccountType.FIXED_TERM.equalValue(dto.getAccountType().getCode())) {
            if (!(31 >= dayAllowed && dayAllowed > 0)) {
              sink.error(new BadRequestException(
                  getMsg("account.day.allowed.invalid", dto.getAccountType().getDescription())));
            }
          } else {
            sink.complete();
          }
        })
        .thenReturn(accountDto);
  }

  /**
   * Find the account by id, then map the accountDto to an entity, then set the id
   * of the entity to the
   * id of the account, then save the entity, then map the entity to a dto.
   *
   * @param id         The id of the account to update
   * @param accountDto The DTO object that will be used to update the account.
   * @return A Mono of AccountDto
   */
  @Override
  public Mono<AccountDto> update(String id, AccountDto accountDto) {
    return accountRepository.findById(id)
        .flatMap(p -> Mono.just(accountDto)
            .map(accountMapper::toEntity)
            .doOnNext(e -> e.setId(id)))
        .flatMap(this.accountRepository::save)
        .map(accountMapper::toDto);
  }

  /**
   * It deletes an account from the database
   *
   * @param id The id of the account to delete.
   * @return A Mono of Void.
   */
  @Override
  public Mono<Void> delete(String id) {
    return accountRepository.deleteById(id);
  }

  /**
   * It finds an account by id, if it doesn't exist it throws an exception, if it
   * does exist it updates
   * the amount and saves it
   *
   * @param accountId The account ID to update
   * @param amount    The amount to be added to the account.
   * @return A Mono of AccountDto
   */
  @Override
  public Mono<AccountDto> updateAccountAmount(String accountId, Double amount) {
    return accountRepository.findById(accountId)
        .switchIfEmpty(Mono.error(new Exception(getMsg("account.not.found"))))
        .map(account -> {
          double totalAmount = Double.sum(amount, account.getAmount());
          account.setAmount(totalAmount);
          account.setLastModifiedDate(LocalDateTime.now());
          return account;
        })
        .flatMap(accountRepository::updateAccountAmount)
        .map(accountMapper::toDto);

  }

}
