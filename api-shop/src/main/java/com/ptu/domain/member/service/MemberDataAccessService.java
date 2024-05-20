package com.ptu.domain.member.service;

import com.ptu.common.infrastructure.SystemDateTimeHolder;
import com.ptu.domain.auth.domain.OAuth2Info;
import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.entity.MemberEntity;
import com.ptu.domain.member.entity.MemberSocialLoginEntity;
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
        .orElseThrow(MemberSocialLoginNotFound::new)
        .toDomain();
  }

  @Transactional
  public void updateLastLoginAt(final String username) {
    MemberSocialLoginEntity memberSocialLoginEntity =
        memberSocialLoginRepository
            .findByUsername(username)
            .orElseThrow(MemberSocialLoginNotFound::new);
    memberSocialLoginEntity.updateLastLoginAt(username, dateTimeHolder.now());
  }

  public MemberSocialLogin signUp(final OAuth2Info oAuth2Info) {
    MemberEntity memberEntity = MemberEntity.of(oAuth2Info, dateTimeHolder.now());
    memberEntity = memberRepository.save(memberEntity);
    return memberEntity.getSocialLogins().get(0).toDomain();
  }
}
