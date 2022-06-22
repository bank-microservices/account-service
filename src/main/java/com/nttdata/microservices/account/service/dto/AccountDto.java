package com.nttdata.microservices.account.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nttdata.microservices.account.entity.AccountType;
import com.nttdata.microservices.account.entity.credit.CreditCard;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

  @NotBlank(message = "clientDocumentNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String clientDocumentNumber;

  @NotBlank(message = "accountNumber is required")
  private String accountNumber;

  @NotBlank(message = "cci is required")
  private String cci;

  @NotNull(message = "amount is required")
  @PositiveOrZero(message = "amount must be greater or equal to zero (0)")
  private Double amount;

  @NotNull(message = "amount is required")
  @PositiveOrZero(message = "maintenanceFee must be greater or equal to zero (0)")
  private Double maintenanceFee;

  @NotNull(message = "maxLimitMonthlyMovements is required")
  @PositiveOrZero(message = "maxLimitMonthlyMovements must be greater or equal to zero (0)")
  private Integer maxLimitMonthlyMovements;

  @NotBlank(message = "accountTypeCode is required")
  @JsonProperty(access = WRITE_ONLY)
  private String accountTypeCode;

  @NotNull(message = "dayAllowed is required")
  @PositiveOrZero(message = "dayAllowed must be greater or equal to zero (0)")
  private Integer dayAllowed;

  private List<@Valid OwnerDto> owners;

  private List<@Valid SignatoryDto> signatories;

  @JsonProperty(access = READ_ONLY)
  private String id;

  @JsonProperty(access = READ_ONLY)
  private Double transactionFee;

  @JsonProperty(access = READ_ONLY)
  private AccountType accountType;

  @JsonProperty(access = READ_ONLY)
  private ClientDto client;

  @JsonProperty(access = READ_ONLY)
  private CreditCard creditCard;

  @JsonProperty(access = READ_ONLY)
  private LocalDateTime createAt;

  @JsonProperty(access = READ_ONLY)
  private Boolean status;

}
