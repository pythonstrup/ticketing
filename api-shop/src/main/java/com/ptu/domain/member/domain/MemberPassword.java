package com.ptu.domain.member.domain;

import com.ptu.common.entity.BaseEntity;
import com.ptu.common.model.BaseDomain;
import com.ptu.domain.member.entity.MemberPasswordEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberPassword extends BaseDomain {

  private MemberSocialLogin socialLogin;
  private String salt;
  private String password;

  @Builder // test only
  private MemberPassword(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberSocialLogin socialLogin,
      final String salt,
      final String password) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.socialLogin = socialLogin;
    this.salt = salt;
    this.password = password;
  }

  private MemberPassword(
      final BaseEntity baseEntity,
      final MemberSocialLogin socialLogin,
      final String salt,
      final String password) {
    super(baseEntity);
    this.socialLogin = socialLogin;
    this.salt = salt;
    this.password = password;
  }

  public static MemberPassword of(final MemberPasswordEntity entity) {
    return new MemberPassword(
        entity, entity.getSocialLogin().toDomain(), entity.getSalt(), entity.getPassword());
  }
}
