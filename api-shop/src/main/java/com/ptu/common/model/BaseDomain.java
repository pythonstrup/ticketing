package com.ptu.common.model;

import com.ptu.common.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public abstract class BaseDomain extends TimeDomain {

  private Long id;

  protected BaseDomain(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id) {
    super(createId, createdAt, updateId, updatedAt, deletedAt);
    this.id = id;
  }

  protected BaseDomain(final BaseEntity baseEntity) {
    super(
        baseEntity.getCreateId(),
        baseEntity.getCreatedAt(),
        baseEntity.getUpdateId(),
        baseEntity.getUpdatedAt(),
        baseEntity.getDeletedAt());
    this.id = baseEntity.getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseDomain that = (BaseDomain) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
