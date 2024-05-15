package com.ptu.domain.auth.service;

import com.ptu.common.exception.ClientRuntimeException;
import com.ptu.domain.auth.domain.ClientUserDetails;
import com.ptu.domain.member.domain.MemberSocialLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final AuthFilterService authFilterService;

  @Override
  public UserDetails loadUserByUsername(String userId) {
    try {
      MemberSocialLogin socialLogin = authFilterService.getSocialLoginByUserId(userId);
      return ClientUserDetails.of(socialLogin);
    } catch (ClientRuntimeException e) {
      throw new AuthenticationServiceException(
          "아이디나 비밀번호가 일치하지 않습니다. 아이디 찾기 혹은 비밀번호 찾기를 시도하시거나 다시 입력해주세요.", e);
    }
  }
}
