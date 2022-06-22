package com.nttdata.microservices.account.config;

import com.nttdata.microservices.account.entity.AccountType;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.mapping.DocumentPointer;

@ReadingConverter
public enum AccountReadConverter implements Converter<AccountType, DocumentPointer<ObjectId>> {
  INSTANCE;

  public DocumentPointer<ObjectId> convert(AccountType source) {
    return () -> new ObjectId(source.getId());
  }
}