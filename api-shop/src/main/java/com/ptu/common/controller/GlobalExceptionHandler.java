package com.ptu.common.controller;

import com.ptu.common.controller.dto.ClientErrorResponse;
import com.ptu.common.exception.ClientRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ClientErrorResponse> handleValidationException(
      final MethodArgumentNotValidException e) {
    return makeErrorResponse(
        "40000",
        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ClientRuntimeException.class)
  public ResponseEntity<ClientErrorResponse> handleCustomMessageRuntimeException(
      final ClientRuntimeException e) {
    return makeErrorResponse(e.getCode(), e.getMessage(), e.getHttpStatus());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ClientErrorResponse> handleAuthenticationException(
      final AuthenticationException e) {
    log.info("Authentication Exception ", e);
    return makeErrorResponse("40100", e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ClientErrorResponse> handleAccessDeniedException(
      final AccessDeniedException e) {
    log.info("AccessDenied Exception ", e);
    return makeErrorResponse("40300", e.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ClientErrorResponse> handleInternalServerError(final RuntimeException e) {
    log.error("Uncontrolled Exception ", e);
    return makeErrorResponse(
        "50000",
        "알 수 없는 오류가 발생했어요.\n다시 시도하거나 로하스밀 고객센터로 문의해주세요.",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ClientErrorResponse> makeErrorResponse(
      String code, String message, HttpStatus status) {
    ClientErrorResponse response = new ClientErrorResponse(code, message);
    return new ResponseEntity<>(response, status);
  }
}
