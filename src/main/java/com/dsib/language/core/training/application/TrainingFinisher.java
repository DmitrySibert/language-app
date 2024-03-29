package com.dsib.language.core.training.application;

import com.dsib.language.core.common.event.DomainEventsBus;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingDomainEvent;
import com.dsib.language.core.training.domain.TrainingRepository;
import com.dsib.language.core.training.presentation.FinishTrainingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class TrainingFinisher {

  private final TrainingRepository trainingRepository;
  private final DomainEventsBus domainEventsBus;
  private final TransactionTemplate transactionTemplate;
  private final static String TRANSACTION_TEMPLATE_NAME = "TrainingFinisherTT";

  public TrainingFinisher(
    TrainingRepository trainingRepository, DomainEventsBus domainEventsBus, TransactionTemplate transactionTemplate
  ) {
    this.trainingRepository = trainingRepository;
    this.domainEventsBus = domainEventsBus;
    this.transactionTemplate = transactionTemplate;
    transactionTemplate.setName(TRANSACTION_TEMPLATE_NAME);
  }

  public Training finishTrainingSession(FinishTrainingDto trainingData) {
    FinishTrainingSessionTransactionResult transactionResult = transactionTemplate.execute(status -> {
      Training training = trainingRepository.find(trainingData.getId())
        .orElseThrow(() -> new IllegalStateException("Training not found: " + trainingData.getId()));
      TrainingDomainEvent event =
        training.complete(trainingData.getApprovedWords(), trainingData.getWordsForRepetition());
      Training updatedTraining = trainingRepository.update(training);
      return new FinishTrainingSessionTransactionResult(event, updatedTraining);
    });
    domainEventsBus.publish(transactionResult.getDomainEvent());
    return transactionResult.getTraining();
  }

  @Data
  @AllArgsConstructor
  //TODO: make generic
  private class FinishTrainingSessionTransactionResult {
    private TrainingDomainEvent domainEvent;
    private Training training;
  }
}
