package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.entity.MemberEntity;
import com.ptu.domain.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public MemberEntity save(final MemberEntity memberEntity) {
    return memberJpaRepository.save(memberEntity);
  }
}
