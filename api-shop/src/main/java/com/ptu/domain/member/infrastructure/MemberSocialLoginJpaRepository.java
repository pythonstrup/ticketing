package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.entity.MemberSocialLoginEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialLoginJpaRepository
    extends JpaRepository<MemberSocialLoginEntity, Long> {

  Optional<MemberSocialLoginEntity> findByUsername(String username);
}
