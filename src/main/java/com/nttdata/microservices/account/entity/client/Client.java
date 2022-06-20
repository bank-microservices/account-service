package com.nttdata.microservices.account.entity.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor
public class Client {

  private String id;
  @ReadOnlyProperty
  private String firstNameBusiness;
  @ReadOnlyProperty
  private String surnames;
  private String documentNumber;
  private ClientType clientType;
  private ClientProfile clientProfile;
}
