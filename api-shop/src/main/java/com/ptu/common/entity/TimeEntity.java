package com.ptu.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class TimeEntity {

  private String createId;

  @CreatedDate private LocalDateTime createdAt;

  private String updateId;

  @LastModifiedDate private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  protected TimeEntity(
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

  protected void register(final String userId) {
    this.createId = userId;
    this.updateId = userId;
  }

  protected void update(final String userId) {
    this.updateId = userId;
  }

  protected void deleteSoftly(final String userId, LocalDateTime now) {
    this.updateId = userId;
    this.deletedAt = now;
  }
}
