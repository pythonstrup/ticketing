package com.ptu.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientRuntimeException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String code;

  public ClientRuntimeException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    httpStatus = errorCode.getHttpStatus();
    code = errorCode.getCode();
  }

  public ClientRuntimeException(ErrorCode errorCode, Object... args) {
    super(String.format(errorCode.getMessage(), args));
    httpStatus = errorCode.getHttpStatus();
    code = errorCode.getCode();
  }
}
