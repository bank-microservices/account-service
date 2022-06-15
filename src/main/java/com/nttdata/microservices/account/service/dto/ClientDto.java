package com.nttdata.microservices.account.service.dto;

import com.nttdata.microservices.account.entity.client.ClientType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDto {

  private String id;
  private String firstNameBusiness;
  private String surnames;
  private String documentNumber;
  private ClientType clientType;

}
