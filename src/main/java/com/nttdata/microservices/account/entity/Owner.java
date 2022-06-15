package com.nttdata.microservices.account.entity;

import lombok.Data;

@Data
public class Owner {
  private String documentNumber;
  private String firstName;
  private String lastName;
}
