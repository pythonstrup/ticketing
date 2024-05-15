package com.ptu.common.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class TimeDomain {

  private final String createId;

  private final LocalDateTime createdAt;

  private final String updateId;

  private final LocalDateTime updatedAt;

  private final LocalDateTime deletedAt;

  protected TimeDomain(
      final String createId,
      final LocalDateTime createdAt,
      final String updateId,
      final LocalDateTime updatedAt,
      final LocalDateTime deletedAt) {
    this.createId = createId;
    this.createdAt = createdAt;
    this.updateId = updateId;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }
}
