package com.nttdata.microservices.account.service.mapper;

import com.nttdata.microservices.account.entity.AccountType;
import com.nttdata.microservices.account.service.dto.AccountTypeDto;
import com.nttdata.microservices.account.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper extends EntityMapper<AccountTypeDto, AccountType> {

}

