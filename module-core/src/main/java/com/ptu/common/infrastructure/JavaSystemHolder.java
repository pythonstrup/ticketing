package com.ptu.common.infrastructure;

import com.ptu.common.service.port.SystemHolder;
import org.springframework.stereotype.Component;

@Component
public class JavaSystemHolder implements SystemHolder {

  @Override
  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }
}
