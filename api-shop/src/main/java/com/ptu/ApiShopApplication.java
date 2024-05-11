package com.ptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ptu")
public class ApiShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiShopApplication.class, args);
  }
}
