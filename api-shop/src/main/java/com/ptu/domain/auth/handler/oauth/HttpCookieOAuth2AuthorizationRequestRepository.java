package com.ptu.domain.auth.handler.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptu.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component("httpCookieOAuth2AuthorizationRequestRepository")
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2AuthRequest";
  public static final String REDIRECT_PATH_PARAM_COOKIE_NAME = "redirectPath";
  private static final int cookieExpireSeconds = 180;

  private final ObjectMapper objectMapper;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(final HttpServletRequest request) {
    return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        .map(
            cookie ->
                CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class, objectMapper))
        .orElse(null);
  }

  @Override
  public void saveAuthorizationRequest(
      final OAuth2AuthorizationRequest authorizationRequest,
      final HttpServletRequest request,
      final HttpServletResponse response) {
    if (authorizationRequest == null) {
      CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
      CookieUtils.deleteCookie(request, response, REDIRECT_PATH_PARAM_COOKIE_NAME);
      return;
    }
    CookieUtils.addCookie(
        response,
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
        CookieUtils.serialize(authorizationRequest),
        cookieExpireSeconds);
    String redirectUriAfterLogin = request.getParameter(REDIRECT_PATH_PARAM_COOKIE_NAME);
    if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
      CookieUtils.addCookie(
          response, REDIRECT_PATH_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(
      final HttpServletRequest request, final HttpServletResponse response) {
    return this.loadAuthorizationRequest(request);
  }

  public void removeAuthorizationRequestCookies(
      HttpServletRequest request, HttpServletResponse response) {
    CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    CookieUtils.deleteCookie(request, response, REDIRECT_PATH_PARAM_COOKIE_NAME);
  }
}
