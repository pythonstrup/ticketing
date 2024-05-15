package com.ptu.domain.auth.service;

import com.ptu.common.service.port.SystemHolder;
import com.ptu.domain.auth.domain.ClientUserDetails;
import com.ptu.domain.auth.domain.TokenType;
import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.service.MemberDataAccessService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class TokenProvider implements InitializingBean {

  private static final String MEMBER_ID = "memberId";
  private static final String MEMBER_SOCIAL_LOGIN_ID = "memberSocialLoginId";
  private static final String USERNAME = "username";
  private static final String AUTHORITY_KEY = "auth";
  private final String SALT;
  private final MemberDataAccessService memberDataAccessService;
  private final SystemHolder systemHolder;
  private Key encodedKey;

  public TokenProvider(
      @Value("${token.secret.key}") final String salt,
      final MemberDataAccessService memberDataAccessService,
      final SystemHolder systemHolder) {
    this.SALT = salt;
    this.memberDataAccessService = memberDataAccessService;
    this.systemHolder = systemHolder;
  }

  @Override
  public void afterPropertiesSet() {
    byte[] keyBytes = Decoders.BASE64.decode(SALT);
    this.encodedKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(
      final TokenType tokenType,
      final Authentication authentication,
      final SystemHolder systemHolder) {
    long now = systemHolder.currentTimeMillis();
    ClientUserDetails principal = (ClientUserDetails) authentication.getPrincipal();
    Map<String, Object> payload = new HashMap<>();
    payload.put(MEMBER_ID, principal.getMemberId());
    payload.put(MEMBER_SOCIAL_LOGIN_ID, principal.getMemberSocialLoginId());
    payload.put(USERNAME, principal.getUsername());
    payload.put(
        AUTHORITY_KEY,
        principal.getAuthorities().stream()
            .findAny()
            .orElseGet(() -> new SimpleGrantedAuthority("ROLE_USER"))
            .getAuthority());

    return Jwts.builder()
        .setSubject(authentication.getName())
        .setClaims(payload)
        .signWith(SignatureAlgorithm.HS256, encodedKey)
        .setExpiration(new Date(now + tokenType.getExpires()))
        .compact();
  }

  public String generateToken(final TokenType tokenType, final MemberSocialLogin socialLogin) {
    long now = systemHolder.currentTimeMillis();
    Map<String, Object> payload = new HashMap<>();
    payload.put(MEMBER_ID, socialLogin.getMember().getId());
    payload.put(MEMBER_SOCIAL_LOGIN_ID, socialLogin.getId());
    payload.put(USERNAME, socialLogin.getUsername());
    payload.put(AUTHORITY_KEY, socialLogin.getMember().getRole());

    return Jwts.builder()
        .setSubject(socialLogin.getUsername())
        .setClaims(payload)
        .signWith(SignatureAlgorithm.HS256, encodedKey)
        .setExpiration(new Date(now + tokenType.getExpires()))
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(token).getBody();
    List<GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITY_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    String username = getUsername(token);
    MemberSocialLogin socialLogin = memberDataAccessService.getSocialLoginByUsername(username);
    ClientUserDetails principal = ClientUserDetails.of(socialLogin);
    return new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {

      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {

      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {

      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

  public ResponseCookie makeExpiredTokenCookie(TokenType tokenType, String token) {
    return ResponseCookie.from(tokenType.getKey(), token)
        .httpOnly(true)
        .sameSite("None")
        .secure(true)
        .maxAge(0)
        .path("/")
        .build();
  }

  public void setTokenToHeaderByLogin(
      final HttpServletResponse response, final String accessToken, final String refreshToken) {
    ResponseCookie accessTokenCookie = makeTokenCookie(TokenType.ACCESS, accessToken);
    ResponseCookie refreshTokenCookie = makeTokenCookie(TokenType.REFRESH, refreshToken);
    response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()); // 쿠키에 값이 쌓이지 않도록 초기화
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
  }

  private ResponseCookie makeTokenCookie(final TokenType tokenType, final String token) {
    return ResponseCookie.from(tokenType.getKey(), token)
        .httpOnly(true)
        .sameSite("None")
        .secure(true)
        .maxAge(tokenType.getExpires())
        .path("/")
        .build();
  }

  public String getUsername(String token) {
    String apiKey = getValue(USERNAME, token);
    if (StringUtils.hasText(apiKey)) {
      return apiKey;
    }
    return "";
  }

  public String getValue(String key, String token) {
    Claims claims = getClaims(token);

    Object value = claims.get(key);
    if (value != null) {
      return value.toString();
    }
    return "";
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(token).getBody();
  }
}
