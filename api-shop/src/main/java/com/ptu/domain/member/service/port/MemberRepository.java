package com.ptu.domain.member.service.port;

import com.ptu.domain.member.entity.MemberEntity;

public interface MemberRepository {

  MemberEntity save(MemberEntity memberEntity);
}
