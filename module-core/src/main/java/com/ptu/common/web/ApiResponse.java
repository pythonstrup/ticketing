package com.ptu.common.web;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

  private final String code;
  private final String message;
  private final T data;

  private ApiResponse(String code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static ApiResponse<EmptyResponse> NO_CONTENT() {
    return new ApiResponse<>("", "", new EmptyResponse());
  }

  public static <T> ApiResponse<T> OK(T data) {
    return new ApiResponse<>("", "OK", data);
  }
}
