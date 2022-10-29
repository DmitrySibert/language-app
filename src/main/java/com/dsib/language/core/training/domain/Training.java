package com.dsib.language.core.training.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static com.dsib.language.core.common.util.AssertUtils.assertNotNull;

public class Training {

  @Getter
  private String id;
  @Getter
  private String ownerId;
  @Getter
  private TrainingStatus status;
  @Getter
  private final TrainingType type;
  @Getter
  private final Integer size;
  @Getter
  private final List<String> tags;
  @Getter
  private final LocalDateTime createdAt;
  @Getter
  private LocalDateTime completedAt;

  @Getter
  @Setter
  private TrainingSet trainingSet;

  public Training(
    String id, String ownerId, TrainingStatus status, TrainingType type, Integer size,
    List<String> tags, LocalDateTime createdAt, List<String> words
  ) {
    assertNotNull("id", id);
    assertNotNull("ownerId", ownerId);
    assertNotNull("status", status);
    assertNotNull("type", type);
    assertNotNull("tags", tags);
    assertNotNull("createdAt", createdAt);
    this.id = id;
    this.ownerId = ownerId;
    this.status = status;
    this.type = type;
    this.size = size;
    this.tags = tags;
    this.createdAt = createdAt;
    trainingSet = new TrainingSet(words);
    completedAt = null;
  }

  public Training(
    String id, String ownerId, TrainingStatus status, TrainingType type, Integer size,
    List<String> tags, LocalDateTime createdAt, TrainingSet trainingSet
  ) {
    this(id, ownerId, status, type, size, tags, createdAt, trainingSet.getWords());
    this.trainingSet.setApproved(trainingSet.getApproved());
    this.trainingSet.setFailed(trainingSet.getFailed());
  }

  //TODO: check words to approve and to fail they are part of this training session
  public TrainingDomainEvent complete(List<String> approved, List<String> failed) {
    setStatus(TrainingStatus.COMPLETED);
    setCompletedAt(LocalDateTime.now());
    trainingSet.setApproved(approved);
    trainingSet.setFailed(failed);
    trainingSet.getWords().removeAll(trainingSet.getApproved());
    trainingSet.getWords().removeAll(trainingSet.getFailed());

    return new TrainingDomainEvent(this.id, this.status);
  }

  public void setStatus(TrainingStatus status) {
    validateStatusChange();
    assertNotNull("status", status);
    this.status = status;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    assertNotNull("completedAt", completedAt);
    this.completedAt = completedAt;
  }

  public void setId(String id) {
    if (this.id != null) {
      throw new IllegalStateException("Id can't be overwrited");
    }
    this.id = id;
  }

  private void validateStatusChange() {
    if (TrainingStatus.COMPLETED.equals(status)) {
      throw new IllegalStateException("Can't change status from " + TrainingStatus.COMPLETED + " to " + status);
    }
  }
}
