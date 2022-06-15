package com.nttdata.microservices.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account_type")
public class AccountType extends AbstractAuditingEntity {

  private String id;

  @Indexed(unique = true)
  private String code;

  private String description;

}
