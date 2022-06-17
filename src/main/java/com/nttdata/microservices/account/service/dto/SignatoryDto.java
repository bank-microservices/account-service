package com.nttdata.microservices.account.service.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignatoryDto {

  @NotNull(message = "documentNumber is required")
  private String documentNumber;

  @NotNull(message = "firstName is required")
  private String firstName;

  @NotNull(message = "lastName is required")
  private String lastName;
}
