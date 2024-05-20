package com.ptu.domain.member.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.ptu.common.entity.BaseEntity;
import com.ptu.domain.auth.domain.OAuth2Info;
import com.ptu.domain.member.domain.MemberProfile;
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
@Table(name = "member_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberProfileEntity extends BaseEntity {

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity member;

  private String name;
  private String mobile;

  @Builder // test only
  public MemberProfileEntity(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberEntity member,
      final String name,
      final String mobile) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.member = member;
    this.name = name;
    this.mobile = mobile;
  }

  public MemberProfileEntity(
      final MemberEntity member, final String name, final String mobile, final String username) {
    this.member = member;
    this.name = name;
    this.mobile = mobile;
    register(username);
  }

  public MemberProfile toDomain() {
    return MemberProfile.of(this);
  }

  public static MemberProfileEntity of(final OAuth2Info oAuth2Info) {
    return new MemberProfileEntity(
        null, oAuth2Info.getName(), oAuth2Info.getMobile(), oAuth2Info.getUsername());
  }
}
