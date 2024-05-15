package com.ptu.domain.auth.controller;

import com.ptu.common.web.ApiResponse;
import com.ptu.domain.auth.controller.dto.request.SignUpOAuthRequest;
import com.ptu.domain.auth.controller.dto.request.SignUpRequest;
import com.ptu.domain.auth.controller.dto.response.SignUpResponse;
import com.ptu.domain.auth.domain.TokenType;
import com.ptu.domain.auth.service.AuthService;
import com.ptu.domain.auth.service.TokenProvider;
import com.ptu.domain.auth.service.dto.SignUpDto;
import com.ptu.domain.member.domain.MemberSocialLogin;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.v1}/auth")
public class AuthController {

  private final AuthService authService;
  private final TokenProvider tokenProvider;

  @PostMapping("/signup")
  public ApiResponse<SignUpResponse> signUp(
      HttpServletResponse httpServletResponse, @Valid @RequestBody SignUpRequest request) {
    SignUpDto dto = authService.signUp(request);
    issueToken(httpServletResponse, dto.socialLogin());
    return ApiResponse.OK(SignUpResponse.of(dto.socialLogin()));
  }

  @PostMapping("/signup/oauth")
  public ApiResponse<SignUpResponse> signUpForOAuth(
      HttpServletResponse httpServletResponse, @Valid @RequestBody SignUpOAuthRequest request) {
    SignUpDto dto = authService.signUpOAuth(request);
    issueToken(httpServletResponse, dto.socialLogin());
    return ApiResponse.OK(SignUpResponse.of(dto.socialLogin()));
  }

  private void issueToken(
      final HttpServletResponse httpServletResponse, final MemberSocialLogin login) {
    String accessToken = tokenProvider.generateToken(TokenType.ACCESS, login);
    String refreshToken = tokenProvider.generateToken(TokenType.REFRESH, login);
    tokenProvider.setTokenToHeaderByLogin(httpServletResponse, accessToken, refreshToken);
  }
}
