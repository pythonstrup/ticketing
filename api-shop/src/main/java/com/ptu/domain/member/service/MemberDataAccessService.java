package com.ptu.domain.member.service;

import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.exception.MemberSocialLoginNotFound;
import com.ptu.domain.member.service.port.MemberRepository;
import com.ptu.domain.member.service.port.MemberSocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDataAccessService {

  private final MemberRepository memberRepository;
  private final MemberSocialLoginRepository memberSocialLoginRepository;

  public MemberSocialLogin getSocialLoginByUsername(final String username) {
    return memberSocialLoginRepository
        .findByUsername(username)
        .orElseThrow(MemberSocialLoginNotFound::new)
        .toDomain();
  }
}
