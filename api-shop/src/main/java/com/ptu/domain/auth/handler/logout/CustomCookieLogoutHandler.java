package com.ptu.domain.auth.handler.logout;

import com.ptu.domain.auth.domain.TokenType;
import com.ptu.domain.auth.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomCookieLogoutHandler implements LogoutHandler {

  private final TokenProvider tokenProvider;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    ResponseCookie access = tokenProvider.makeExpiredTokenCookie(TokenType.ACCESS, null);
    ResponseCookie refresh = tokenProvider.makeExpiredTokenCookie(TokenType.REFRESH, null);
    response.addHeader(HttpHeaders.SET_COOKIE, access.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
  }
}
