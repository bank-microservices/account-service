package com.nttdata.microservices.account.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeDto {

  @JsonProperty(access = READ_ONLY)
  private String id;

  @NotBlank(message = "code is required")
  private String code;

  @NotBlank(message = "description is required")
  private String description;

}
