package com.ptu.domain.member.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.auth.domain.OAuth2Info;
import jakarta.persistence.CascadeType;
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
public class MemberSocialLogin extends BaseEntity {

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToOne(fetch = LAZY, mappedBy = "socialLogin", cascade = CascadeType.PERSIST)
  private MemberPassword password;

  private String username;

  @Enumerated(EnumType.STRING)
  private SocialLoginType socialLoginType;

  private LocalDateTime lastLoginAt;

  @Builder // test only
  public MemberSocialLogin(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final Member member,
      final MemberPassword password,
      final String username,
      final SocialLoginType socialLoginType) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.member = member;
    this.password = password;
    this.username = username;
    this.socialLoginType = socialLoginType;
  }

  public MemberSocialLogin(
      final String username,
      final SocialLoginType socialLoginType,
      final LocalDateTime lastLoginAt) {
    this.username = username;
    this.socialLoginType = socialLoginType;
    this.lastLoginAt = lastLoginAt;
  }

  public void updateLastLoginAt(final String username, final LocalDateTime now) {
    update(username);
    this.lastLoginAt = now;
    this.member.updateLastLoginAt(username, now);
  }

  public static MemberSocialLogin of(final OAuth2Info oAuth2Info, LocalDateTime now) {
    MemberSocialLogin socialLogin =
        new MemberSocialLogin(oAuth2Info.getUsername(), oAuth2Info.getLoginType(), now);
    MemberPassword memberPassword = MemberPassword.empty();
    memberPassword.setSocialLogin(socialLogin);
    return socialLogin;
  }

  public void setMember(final Member member) {
    this.member = member;
    member.addSocialLogin(this);
  }

  public void setPassword(final MemberPassword memberPassword) {
    this.password = memberPassword;
  }
}
