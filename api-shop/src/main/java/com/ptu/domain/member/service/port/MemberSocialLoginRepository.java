package com.ptu.domain.member.service.port;

import com.ptu.domain.member.entity.MemberSocialLoginEntity;
import java.util.Optional;

public interface MemberSocialLoginRepository {

  Optional<MemberSocialLoginEntity> findByUsername(String username);
}
