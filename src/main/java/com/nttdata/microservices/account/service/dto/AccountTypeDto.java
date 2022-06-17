package com.nttdata.microservices.account.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
