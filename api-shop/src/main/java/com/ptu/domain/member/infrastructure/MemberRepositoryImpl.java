package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.domain.Member;
import com.ptu.domain.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public Member save(final Member member) {
    return memberJpaRepository.save(member);
  }
}
