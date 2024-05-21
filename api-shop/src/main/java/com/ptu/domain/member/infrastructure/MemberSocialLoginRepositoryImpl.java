package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.service.port.MemberSocialLoginRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberSocialLoginRepositoryImpl implements MemberSocialLoginRepository {

  private final MemberSocialLoginJpaRepository memberSocialLoginJpaRepository;

  @Override
  public Optional<MemberSocialLogin> findByUsername(final String username) {
    return memberSocialLoginJpaRepository.findByUsername(username);
  }
}
