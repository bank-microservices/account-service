package com.nttdata.microservices.account.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceDto {

  private String accountType;
  private String accountNumber;
  private Double balance;

}
