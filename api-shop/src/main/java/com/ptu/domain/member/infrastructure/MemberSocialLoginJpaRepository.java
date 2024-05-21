package com.ptu.domain.member.infrastructure;

import com.ptu.domain.member.domain.MemberSocialLogin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberSocialLoginJpaRepository extends JpaRepository<MemberSocialLogin, Long> {

  @Query(
      "select msl from MemberSocialLogin msl "
          + "join fetch msl.member m "
          + "where msl.deletedAt is null and msl.username = :username ")
  Optional<MemberSocialLogin> findByUsername(@Param("username") String username);
}
