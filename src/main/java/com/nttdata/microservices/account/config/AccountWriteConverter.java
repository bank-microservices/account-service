package com.nttdata.microservices.account.config;

import com.nttdata.microservices.account.entity.AccountType;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.DocumentPointer;

@WritingConverter
public enum AccountWriteConverter implements Converter<DocumentPointer<Object>, AccountType> {
  INSTANCE;

  @Override
  public AccountType convert(DocumentPointer<Object> source) {

    Object pointer = source.getPointer();
    AccountType accountType = new AccountType();
    if (pointer instanceof ObjectId) {
      ObjectId id = (ObjectId) pointer;
      accountType.setId(id.toHexString());
    } else {
      Document document = (Document) pointer;
      BsonDocument bsonDocument = document.toBsonDocument();
      accountType.setId(bsonDocument.getObjectId("_id").getValue().toString());
      accountType.setCode(bsonDocument.getString("code").getValue());
      accountType.setDescription(bsonDocument.getString("description").getValue());
      accountType.setTransactionFeeBase(bsonDocument.getDouble("transactionFeeBase").getValue());
    }
    return accountType;
  }
}