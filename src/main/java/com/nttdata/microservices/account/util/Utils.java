package com.nttdata.microservices.account.util;

public class Utils {
  public static <T> T defaultIfNull(T object, T defaultValue) {
    return object != null ? object : defaultValue;
  }
}
