package com.ptu.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  @Bean("virtualThreadExecutorService")
  public ExecutorService executorService() {
    return executor;
  }
}
