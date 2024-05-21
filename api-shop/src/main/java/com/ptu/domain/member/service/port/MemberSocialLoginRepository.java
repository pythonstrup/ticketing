package com.ptu.domain.member.service.port;

import com.ptu.domain.member.domain.MemberSocialLogin;
import java.util.Optional;

public interface MemberSocialLoginRepository {

  Optional<MemberSocialLogin> findByUsername(String username);
}
