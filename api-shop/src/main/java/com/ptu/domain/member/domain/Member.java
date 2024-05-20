package com.ptu.domain.member.domain;

import com.ptu.common.entity.BaseEntity;
import com.ptu.common.model.BaseDomain;
import com.ptu.domain.member.entity.MemberEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member extends BaseDomain {

  private final MemberRole role;

  private final MemberProfile memberProfile;

  @Builder // test only
  private Member(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final MemberRole role,
      final MemberProfile memberProfile) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.role = role;
    this.memberProfile = memberProfile;
  }

  private Member(
      final BaseEntity baseEntity, final MemberRole role, final MemberProfile memberProfile) {
    super(baseEntity);
    this.role = role;
    this.memberProfile = memberProfile;
  }

  public static Member of(final MemberEntity entity) {
    return new Member(entity, entity.getRole(), entity.getMemberProfile().toDomain());
  }
}
