package com.ptu.domain.member.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.auth.domain.OAuth2Info;
import com.ptu.domain.member.domain.Member;
import com.ptu.domain.member.domain.MemberRole;
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
public class MemberEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @OneToOne(fetch = LAZY, mappedBy = "member", cascade = CascadeType.PERSIST)
  private MemberProfileEntity memberProfile;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<MemberSocialLoginEntity> socialLogins = new ArrayList<>();

  private LocalDateTime lastLoginAt;

  public Member toDomain() {
    return Member.of(this);
  }

  @Builder // test only
  private MemberEntity(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberRole role,
      final MemberProfileEntity memberProfile,
      final List<MemberSocialLoginEntity> socialLogins,
      final LocalDateTime lastLoginAt) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.role = role;
    this.memberProfile = memberProfile;
    this.socialLogins = socialLogins;
    this.lastLoginAt = lastLoginAt;
  }

  public MemberEntity(
      final MemberRole role,
      final MemberProfileEntity memberProfile,
      final List<MemberSocialLoginEntity> socialLogins,
      final LocalDateTime lastLoginAt) {
    this.role = role;
    this.memberProfile = memberProfile;
    this.socialLogins = socialLogins;
    this.lastLoginAt = lastLoginAt;
  }

  public void updateLastLoginAt(final String username, final LocalDateTime now) {
    update(username);
    this.lastLoginAt = now;
  }

  public static MemberEntity of(final OAuth2Info oAuth2Info, final LocalDateTime now) {
    return new MemberEntity(
        MemberRole.ROLE_USER,
        MemberProfileEntity.of(oAuth2Info),
        List.of(MemberSocialLoginEntity.of(oAuth2Info, now)),
        now);
  }
}
