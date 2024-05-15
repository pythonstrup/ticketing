package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.entity.MemberSocialLoginEntity;
import com.ptu.domain.member.service.port.MemberSocialLoginRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberSocialLoginRepositoryImpl implements MemberSocialLoginRepository {

  private final MemberSocialLoginJpaRepository memberSocialLoginJpaRepository;

  @Override
  public Optional<MemberSocialLoginEntity> findByUsername(final String username) {
    return memberSocialLoginJpaRepository.findByUsername(username);
  }
}
