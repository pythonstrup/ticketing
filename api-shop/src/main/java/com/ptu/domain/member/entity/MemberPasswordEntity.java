package com.ptu.domain.member.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.member.domain.MemberPassword;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberPasswordEntity extends BaseEntity {

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "member_social_login_id")
  private MemberSocialLoginEntity socialLogin;

  private String salt;

  private String password;

  public MemberPassword toDomain() {
    return MemberPassword.of(this);
  }

  @Builder // test only
  public MemberPasswordEntity(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberSocialLoginEntity socialLogin,
      final String salt,
      final String password) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.socialLogin = socialLogin;
    this.salt = salt;
    this.password = password;
  }
}
