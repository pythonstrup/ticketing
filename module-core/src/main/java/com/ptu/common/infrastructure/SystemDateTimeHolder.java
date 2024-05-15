package com.ptu.common.infrastructure;

import com.ptu.common.service.port.DateTimeHolder;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemDateTimeHolder implements DateTimeHolder {

  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
