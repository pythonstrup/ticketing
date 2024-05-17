package com.ptu.domain.auth.handler.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("oAuth2LoginFailureHandler")
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  public OAuth2LoginFailureHandler(@Value("${client.host}/login") String login) {
    super(login);
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    log.error("OAuth Login Fail", exception);
    final Map<String, String[]> parameterMap = request.getParameterMap();
    super.onAuthenticationFailure(request, response, exception);
  }
}
