package com.dsib.language.core.training;

import com.dsib.language.core.common.event.DomainEvent;
import com.dsib.language.core.common.event.DomainEventsBus;
import com.dsib.language.core.training.application.TrainingFinisher;
import com.dsib.language.core.training.domain.*;
import com.dsib.language.core.training.presentation.FinishTrainingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingFinisherTest {

  @Mock
  private TrainingRepository trainingRepository;
  @Mock
  private DomainEventsBus domainEventsBus;
  @Mock
  private TransactionTemplate transactionTemplate;

  private static final String TRAINING_ID = "TRAINING_ID";
  private static final FinishTrainingDto TRAINING_DTO = new FinishTrainingDto(TRAINING_ID, List.of(), List.of());
  private Training training;

  private TrainingFinisher trainingFinisher;

  @BeforeEach
  public void setUp() {
    trainingFinisher = new TrainingFinisher(trainingRepository, domainEventsBus, transactionTemplate);
    training = new Training(
      TRAINING_ID, "ownerId", TrainingStatus.CREATED, TrainingType.RANDOM,
      10, List.of(), LocalDateTime.MIN, List.of()
    );
    when(transactionTemplate.execute(any()))
      .thenAnswer(invocation ->
        invocation.<TransactionCallback<Boolean>>getArgument(0)
          .doInTransaction(mock(TransactionStatus.class))
      );
  }

  @Test
  public void testTrainingUpdated() {
    when(trainingRepository.find(eq(TRAINING_ID))).thenReturn(Optional.of(training));

    trainingFinisher.finishTrainingSession(TRAINING_DTO);

    ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);
    verify(trainingRepository).update(trainingCaptor.capture());
    assertEquals(TRAINING_ID, trainingCaptor.getValue().getId());
  }

  @Test
  public void testEventPublished() {
    when(trainingRepository.find(eq(TRAINING_ID))).thenReturn(Optional.of(training));

    trainingFinisher.finishTrainingSession(TRAINING_DTO);

    ArgumentCaptor<DomainEvent> domainEventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
    verify(domainEventsBus).publish(domainEventCaptor.capture());
    assertEquals("TrainingDomainEvent", domainEventCaptor.getValue().getName());
    assertNotNull(((TrainingDomainEvent) domainEventCaptor.getValue()).getEntityId());
  }

  @Test
  public void testTrainingNotExist() {
    when(trainingRepository.find(eq(TRAINING_ID))).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> trainingFinisher.finishTrainingSession(TRAINING_DTO));
  }

  @Test
  public void testTrainingAlreadyCompleted() {
    training.setStatus(TrainingStatus.COMPLETED);

    assertThrows(IllegalStateException.class, () -> trainingFinisher.finishTrainingSession(TRAINING_DTO));
  }
}
