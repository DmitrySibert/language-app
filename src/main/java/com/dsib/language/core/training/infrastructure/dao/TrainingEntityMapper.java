package com.dsib.language.core.training.infrastructure.dao;

import com.dsib.language.core.training.domain.Training;

import java.util.LinkedList;
import java.util.List;

public class TrainingEntityMapper {

  public Training fromEntity(TrainingEntity trainingEntity) {
    Training training = new Training(
      trainingEntity.getId(), trainingEntity.getOwnerId(), trainingEntity.getStatus(), trainingEntity.getType(),
      trainingEntity.getSize(), trainingEntity.getTags(), trainingEntity.getCreatedAt(), trainingEntity.getTrainingSet()
    );
    if (null != trainingEntity.getCompletedAt()) {
      training.setCompletedAt(trainingEntity.getCompletedAt());
    }
    return training;
  }

  public List<Training> fromEntity(Iterable<TrainingEntity> trainingEntities) {

    List<Training> trainings = new LinkedList<>();
    trainingEntities.forEach(trainingEntity -> trainings.add(fromEntity(trainingEntity)));

    return trainings;
  }

  public TrainingEntity toEntity(Training training) {
    return new TrainingEntity(
      training.getId(), training.getOwnerId(), training.getStatus(), training.getType(), training.getSize(),
      training.getTags(), training.getCreatedAt(), training.getCompletedAt(), training.getTrainingSet(),false
    );
  }

  public TrainingEntity toNewEntity(Training training) {
    return new TrainingEntity(
      training.getId(), training.getOwnerId(), training.getStatus(), training.getType(), training.getSize(),
      training.getTags(), training.getCreatedAt(), training.getCompletedAt(), training.getTrainingSet(), true
    );
  }
}
