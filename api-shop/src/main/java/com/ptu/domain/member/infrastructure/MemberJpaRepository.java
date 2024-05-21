package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {}
