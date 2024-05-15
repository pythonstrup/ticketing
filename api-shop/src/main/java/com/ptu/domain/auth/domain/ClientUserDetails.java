package com.ptu.domain.auth.domain;

import com.ptu.domain.member.domain.MemberRole;
import com.ptu.domain.member.domain.MemberSocialLogin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class ClientUserDetails implements UserDetails {

  private final String username;
  private final String password;
  private final Collection<GrantedAuthority> authorities;
  private final Long memberId;
  private final Long memberSocialLoginId;

  private ClientUserDetails(
      final String username,
      final String password,
      final Collection<GrantedAuthority> authorities,
      final Long memberId,
      final Long memberSocialLoginId) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.memberId = memberId;
    this.memberSocialLoginId = memberSocialLoginId;
  }

  public static ClientUserDetails of(final MemberSocialLogin socialLogin) {
    return new ClientUserDetails(
        socialLogin.getUsername(),
        socialLogin.getPassword().getPassword(),
        getUserAuthorities(socialLogin.getMember().getRole()),
        socialLogin.getMember().getId(),
        socialLogin.getId());
  }

  private static Collection<GrantedAuthority> getUserAuthorities(final MemberRole memberRole) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(memberRole.name()));
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
