package com.dsib.language.core.progress.infrastructure.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("word_progress")
@Data
@AllArgsConstructor
public class WordProgressEntity implements Persistable<String> {

  @Id
  private String origin;
  private String ownerId;
  private int approved;
  private int failed;
  private LocalDateTime updatedAt;
  @Transient
  private boolean isNew;

  public WordProgressEntity() {
  }

  public WordProgressEntity(String origin, String ownerId, int approved, int failed, LocalDateTime updatedAt) {
    this(origin, ownerId, approved, failed, updatedAt, true);
  }

  @Override
  public String getId() {
    return origin;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}
