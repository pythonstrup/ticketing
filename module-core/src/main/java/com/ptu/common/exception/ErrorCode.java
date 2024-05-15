package com.ptu.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  MEMBER_SOCIAL_LOGIN_NOT_FOUND(HttpStatus.NOT_FOUND, "404-1", "회원의 소셜로그인 정보를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
