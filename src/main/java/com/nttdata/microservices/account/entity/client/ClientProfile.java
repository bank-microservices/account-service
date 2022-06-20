package com.nttdata.microservices.account.entity.client;

import java.util.Arrays;

public enum ClientProfile {

  REGULAR, VIP, PYME;

  public boolean in(ClientProfile... clientProfiles) {
    return Arrays.stream(clientProfiles).anyMatch(profile -> profile == this);
  }
}
