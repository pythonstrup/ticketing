package com.ptu.domain.auth.domain;

import com.ptu.domain.auth.exception.NotSupportedOAuthType;
import com.ptu.domain.member.domain.SocialLoginType;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Getter
public class OAuth2Info {

  private final String name;
  private final String email;
  private final String mobile;
  private final SocialLoginType loginType;
  private final String oAuthId;

  private OAuth2Info(
      final Optional<Object> name,
      final Optional<Object> email,
      final Optional<Object> mobile,
      final SocialLoginType loginType,
      final String oAuthId) {
    this.name = name.map(Object::toString).orElse(null);
    this.email = email.map(Object::toString).orElse(null);
    this.mobile = mobile.map(Object::toString).orElse(null);
    this.loginType = loginType;
    this.oAuthId = oAuthId;
  }

  public String getUsername() {
    return loginType.name() + oAuthId;
  }

  public static OAuth2Info tokenOf(String type, OAuth2AuthenticationToken token)
      throws NotSupportedOAuthType {
    if (type.equals("kakao")) {
      return kakaoOf(token);
    } else if (type.equals("naver")) {
      return naverOf(token);
    } else {
      throw new NotSupportedOAuthType();
    }
  }

  private static OAuth2Info kakaoOf(OAuth2AuthenticationToken token) {
    Map<String, Object> kakaoAccount = token.getPrincipal().getAttribute("kakao_account");
    String id = Objects.requireNonNull(token.getPrincipal().getAttribute("id")).toString();
    return new OAuth2Info(
        Optional.ofNullable(kakaoAccount.get("name")),
        Optional.ofNullable(kakaoAccount.get("email")),
        Optional.ofNullable(kakaoAccount.get("phone_number")),
        SocialLoginType.KAKAO,
        id);
  }

  private static OAuth2Info naverOf(OAuth2AuthenticationToken token) {
    Map<String, Object> naverAccount = token.getPrincipal().getAttribute("response");
    final String id = naverAccount.get("id").toString();
    return new OAuth2Info(
        Optional.ofNullable(naverAccount.get("name")),
        Optional.ofNullable(naverAccount.get("email")),
        Optional.ofNullable(naverAccount.get("mobile")),
        SocialLoginType.NAVER,
        id);
  }
}
