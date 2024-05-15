package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {}
