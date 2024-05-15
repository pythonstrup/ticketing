package com.ptu.common.infrastructure;

import com.ptu.common.service.port.DateHolder;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class SystemDateHolder implements DateHolder {

  @Override
  public LocalDate now() {
    return LocalDate.now();
  }
}
