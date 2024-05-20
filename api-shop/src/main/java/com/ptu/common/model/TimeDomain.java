package com.ptu.common.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class TimeDomain {

  private String createId;

  private LocalDateTime createdAt;

  private String updateId;

  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

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
