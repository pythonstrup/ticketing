package com.ptu.domain.member.service;

import com.ptu.common.infrastructure.SystemDateTimeHolder;
import com.ptu.domain.auth.domain.OAuth2Info;
import com.ptu.domain.member.domain.Member;
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

  private final SystemDateTimeHolder dateTimeHolder;
  private final MemberRepository memberRepository;
  private final MemberSocialLoginRepository memberSocialLoginRepository;

  public MemberSocialLogin getSocialLoginByUsername(final String username) {
    return memberSocialLoginRepository
        .findByUsername(username)
        .orElseThrow(MemberSocialLoginNotFound::new);
  }

  @Transactional
  public void updateLastLoginAt(final String username) {
    MemberSocialLogin memberSocialLogin =
        memberSocialLoginRepository
            .findByUsername(username)
            .orElseThrow(MemberSocialLoginNotFound::new);
    memberSocialLogin.updateLastLoginAt(username, dateTimeHolder.now());
  }

  @Transactional
  public MemberSocialLogin signUp(final OAuth2Info oAuth2Info) {
    Member member = Member.of(oAuth2Info, dateTimeHolder.now());
    member = memberRepository.save(member);
    return member.getSocialLogins().get(0);
  }
}
