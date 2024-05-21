package com.ptu.domain.auth.handler.oauth;

import static com.ptu.domain.auth.handler.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_PATH_PARAM_COOKIE_NAME;

import com.ptu.common.exception.ClientAuthenticationException;
import com.ptu.common.service.port.SystemHolder;
import com.ptu.common.utils.CookieUtils;
import com.ptu.domain.auth.domain.ClientUserDetails;
import com.ptu.domain.auth.domain.OAuth2Info;
import com.ptu.domain.auth.domain.TokenType;
import com.ptu.domain.auth.service.TokenProvider;
import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.service.MemberDataAccessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component("oAuth2LoginSuccessHandler")
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  public static final String OAUTH_SIGNUP_PATH = "/oauth/signup";
  public static final String OAUTH_LOGIN_PATH = "/oauth";
  public static final String REDIRECT_PATH = "redirectPath";

  private final String CLIENT_HOST;
  private final SystemHolder systemHolder;
  private final UserDetailsService userDetailsService;
  private final TokenProvider tokenProvider;
  private final HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;
  private final MemberDataAccessService memberDataAccessService;

  public OAuth2LoginSuccessHandler(
      @Value("${client.host}") final String CLIENT_HOST,
      final SystemHolder systemHolder,
      final UserDetailsService userDetailsService,
      final TokenProvider tokenProvider,
      final HttpCookieOAuth2AuthorizationRequestRepository
          httpCookieOAuth2AuthorizationRequestRepository,
      final MemberDataAccessService memberDataAccessService) {
    this.CLIENT_HOST = CLIENT_HOST;
    this.systemHolder = systemHolder;
    this.userDetailsService = userDetailsService;
    this.tokenProvider = tokenProvider;
    this.httpCookieOAuth2AuthorizationRequestRepository =
        httpCookieOAuth2AuthorizationRequestRepository;
    this.memberDataAccessService = memberDataAccessService;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    try {
      OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
      String type = token.getAuthorizedClientRegistrationId();
      OAuth2Info oAuth2Info = OAuth2Info.tokenOf(type, token);
      sendJwtTokenOrOAuth2Info(request, response, oAuth2Info);

    } catch (ClientAuthenticationException e) {
      request.setAttribute("exception", e);
    }
  }

  private void sendJwtTokenOrOAuth2Info(
      HttpServletRequest request, HttpServletResponse response, OAuth2Info oAuth2Info)
      throws IOException, ServletException {
    try {
      UserDetails principal = userDetailsService.loadUserByUsername(oAuth2Info.getUsername());
      setJwtToken(response, principal);
    } catch (AuthenticationException e) {
      MemberSocialLogin memberSocialLogin = memberDataAccessService.signUp(oAuth2Info);
      ClientUserDetails.of(memberSocialLogin);
    } catch (Exception e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    } finally {
      String defaultTargetUrl = getDefaultTargetUrl();
      clearAuthenticationAttributes(request, response);
      getRedirectStrategy().sendRedirect(request, response, defaultTargetUrl);
    }
  }

  private void setJwtToken(HttpServletResponse response, UserDetails principal) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(principal, "");
    String accessToken =
        tokenProvider.generateToken(TokenType.ACCESS, authentication, systemHolder);
    String refreshToken =
        tokenProvider.generateToken(TokenType.REFRESH, authentication, systemHolder);
    tokenProvider.setTokenToHeaderByLogin(response, accessToken, refreshToken);
    response.setStatus(HttpStatus.NO_CONTENT.value());

    ClientUserDetails clientUserDetails = (ClientUserDetails) principal;
    memberDataAccessService.updateLastLoginAt(clientUserDetails.getUsername());
  }

  protected String determineTargetUrl(HttpServletRequest request) {
    String targetPath = getTargetPath(request);
    return UriComponentsBuilder.fromHttpUrl(CLIENT_HOST)
        .path(OAUTH_LOGIN_PATH)
        .queryParam(REDIRECT_PATH, targetPath)
        .build()
        .toUriString();
  }

  private String getTargetPath(final HttpServletRequest request) {
    Optional<String> redirectPath =
        CookieUtils.getCookie(request, REDIRECT_PATH_PARAM_COOKIE_NAME).map(Cookie::getValue);
    return redirectPath.orElse(getDefaultTargetUrl());
  }

  protected void clearAuthenticationAttributes(
      HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(
        request, response);
  }
}
