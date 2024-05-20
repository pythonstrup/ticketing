package com.ptu.common.exception;

import javax.naming.AuthenticationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientAuthenticationException extends AuthenticationException {

  private final HttpStatus status = HttpStatus.UNAUTHORIZED;

  public ClientAuthenticationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
  }
}
