package com.ptu.domain.member.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.auth.domain.OAuth2Info;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @OneToOne(fetch = LAZY, mappedBy = "member", cascade = CascadeType.PERSIST)
  private MemberProfile memberProfile;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<MemberSocialLogin> socialLogins = new ArrayList<>();

  private LocalDateTime lastLoginAt;

  @Builder // test only
  private Member(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberRole role,
      final MemberProfile memberProfile,
      final List<MemberSocialLogin> socialLogins,
      final LocalDateTime lastLoginAt) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.role = role;
    this.memberProfile = memberProfile;
    this.socialLogins = socialLogins;
    this.lastLoginAt = lastLoginAt;
  }

  public Member(final MemberRole role, final LocalDateTime lastLoginAt) {
    this.role = role;
    this.lastLoginAt = lastLoginAt;
  }

  public void updateLastLoginAt(final String username, final LocalDateTime now) {
    update(username);
    this.lastLoginAt = now;
  }

  public void setMemberProfile(final MemberProfile memberProfile) {
    this.memberProfile = memberProfile;
  }

  public void addSocialLogin(final MemberSocialLogin socialLogin) {
    this.socialLogins.add(socialLogin);
  }

  public static Member of(final OAuth2Info oAuth2Info, final LocalDateTime now) {
    Member member = new Member(MemberRole.ROLE_USER, now);

    MemberProfile profile = MemberProfile.of(oAuth2Info);
    profile.setMember(member);

    MemberSocialLogin socialLogin = MemberSocialLogin.of(oAuth2Info, now);
    socialLogin.setMember(member);

    return member;
  }
}
