package com.ptu.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class TimeEntity {

  private String createId;

  @CreatedDate private LocalDateTime createdAt;

  private String updateId;

  @LastModifiedDate private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  public void register(final String userId) {
    this.createId = userId;
    this.updateId = userId;
  }

  public void update(final String userId) {
    this.updateId = userId;
  }

  public void deleteSoftly(final String userId, LocalDateTime now) {
    this.updateId = userId;
    this.deletedAt = now;
  }
}
