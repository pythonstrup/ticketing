package com.ptu.domain.member.domain;

import com.ptu.common.entity.BaseEntity;
import com.ptu.common.model.BaseDomain;
import com.ptu.domain.member.entity.MemberSocialLoginEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSocialLogin extends BaseDomain {

  private final Member member;
  private final String username;
  private final MemberPassword password;
  private SocialLoginType socialLoginType;

  @Builder // test only
  private MemberSocialLogin(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final Member member,
      final String username,
      final MemberPassword password) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.member = member;
    this.username = username;
    this.password = password;
  }

  private MemberSocialLogin(
      final BaseEntity baseEntity,
      final Member member,
      final String username,
      final MemberPassword password) {
    super(baseEntity);
    this.member = member;
    this.username = username;
    this.password = password;
  }

  public static MemberSocialLogin of(final MemberSocialLoginEntity entity) {
    return new MemberSocialLogin(
        entity,
        entity.getMember().toDomain(),
        entity.getUsername(),
        entity.getPassword().toDomain());
  }
}
