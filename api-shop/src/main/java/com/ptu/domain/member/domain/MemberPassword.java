package com.ptu.domain.member.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberPassword extends BaseEntity {

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "member_social_login_id")
  private MemberSocialLogin socialLogin;

  private String salt;

  private String password;

  @Builder // test only
  public MemberPassword(
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

  public MemberPassword(final String salt, final String password) {
    this.salt = salt;
    this.password = password;
  }

  public static MemberPassword empty() {
    return new MemberPassword("", "");
  }

  public void setSocialLogin(final MemberSocialLogin socialLogin) {
    this.socialLogin = socialLogin;
    socialLogin.setPassword(this);
  }
}
