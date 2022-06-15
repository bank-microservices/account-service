package com.nttdata.microservices.account.service.mapper;

import com.nttdata.microservices.account.entity.Account;
import com.nttdata.microservices.account.entity.client.Client;
import com.nttdata.microservices.account.service.dto.AccountDto;
import com.nttdata.microservices.account.service.dto.ClientDto;
import com.nttdata.microservices.account.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper extends EntityMapper<AccountDto, Account> {

  @Mapping(target = "client", source = "client", qualifiedByName = "accountClient")
  AccountDto toDto(Account entity);

  @Named("accountClient")
  ClientDto toDtoClient(Client client);
}

