package com.ptu.domain.member.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.member.domain.MemberSocialLogin;
import com.ptu.domain.member.domain.SocialLoginType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_social_login")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSocialLoginEntity extends BaseEntity {

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity member;

  @OneToOne(fetch = LAZY, mappedBy = "memberSocialLogin")
  private MemberPasswordEntity password;

  private String username;

  @Enumerated(EnumType.STRING)
  private SocialLoginType socialLoginType;

  public MemberSocialLogin toDomain() {
    return MemberSocialLogin.of(this);
  }

  @Builder // test only
  public MemberSocialLoginEntity(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberEntity member,
      final MemberPasswordEntity password,
      final String username,
      final SocialLoginType socialLoginType) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.member = member;
    this.password = password;
    this.username = username;
    this.socialLoginType = socialLoginType;
  }
}
