package com.ptu.domain.auth.service;

import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.service.MemberDataAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthFilterService {

  private final MemberDataAccessService memberDataAccessService;

  public MemberSocialLogin getSocialLoginByUserId(final String username) {
    return memberDataAccessService.getSocialLoginByUsername(username);
  }
}
