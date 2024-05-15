package com.ptu.domain.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
  ACCESS("shopAccess", 24 * 60 * 60 * 1000),
  REFRESH("shopRefresh", 14 * 24 * 60 * 60 * 1000),
  ;

  private final String key;
  private final long expires;
}
