package com.nttdata.microservices.account.entity;

import com.nttdata.microservices.account.entity.client.Client;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Document(collection = "account")
public class Account extends AbstractAuditingEntity {

  @Id
  private String id;

  @Indexed(unique = true)
  private String accountNumber;

  @Indexed(unique = true)
  private String cci;

  private Double amount;
  private Double maintenanceFee;
  private Integer maxLimitMonthlyMovements;
  private Integer dayAllowed;

  @DocumentReference
  private AccountType accountType;

  private Client client;

  private List<Owner> owners;

  private List<Signatory> signatories;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime createAt;
  private Boolean status;

}
