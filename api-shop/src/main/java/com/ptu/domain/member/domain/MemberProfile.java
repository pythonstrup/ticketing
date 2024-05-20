package com.ptu.domain.member.domain;

import com.ptu.common.entity.BaseEntity;
import com.ptu.common.model.BaseDomain;
import com.ptu.domain.member.entity.MemberProfileEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberProfile extends BaseDomain {

  private final Member member;
  private final String name;
  private final String mobile;

  @Builder // test only
  private MemberProfile(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id,
      final Member member,
      final String name,
      final String mobile) {
    super(createId, createdAt, updateId, updatedAt, deletedAt, id);
    this.member = member;
    this.name = name;
    this.mobile = mobile;
  }

  public MemberProfile(
      final BaseEntity baseEntity, final Member member, final String name, final String mobile) {
    super(baseEntity);
    this.member = member;
    this.name = name;
    this.mobile = mobile;
  }

  public static MemberProfile of(final MemberProfileEntity entity) {
    return new MemberProfile(
        entity, entity.getMember().toDomain(), entity.getName(), entity.getMobile());
  }
}
