package com.ptu.domain.member.service.port;

import com.ptu.domain.member.domain.Member;

public interface MemberRepository {

  Member save(Member member);
}
