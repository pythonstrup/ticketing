package com.ptu.common.model;

import com.ptu.common.entity.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseDomain extends TimeDomain {

  private final Long id;

  public BaseDomain(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt,
      final Long id) {
    super(createId, createdAt, updateId, updatedAt, deletedAt);
    this.id = id;
  }

  public BaseDomain(final BaseEntity baseEntity) {
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
