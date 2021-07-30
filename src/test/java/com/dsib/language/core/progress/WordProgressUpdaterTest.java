package com.dsib.language.core.progress;

import com.dsib.language.core.common.event.DomainEventsBus;
import com.dsib.language.core.common.event.SimpleDomainEventsBus;
import com.dsib.language.core.progress.application.WordProgressUpdater;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressRepository;
import com.dsib.language.core.training.application.TrainingProvider;
import com.dsib.language.core.training.domain.*;
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
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordProgressUpdaterTest {

  private static final String EMPTY_STRING = "";

  @Mock
  private WordProgressRepository wordProgressRepository;
  @Mock
  private TrainingProvider trainingProvider;
  @Mock
  private TransactionTemplate transactionTemplate;
  private DomainEventsBus domainEventsBus;

  private WordProgressUpdater wordProgressUpdater;

  private final String word1 = "word1";
  private final String word2 = "word2";
  private final String word3 = "word3";
  private final Training training = new Training(
    "id", TrainingStatus.COMPLETED, TrainingType.RANDOM, Integer.MAX_VALUE, List.of(), LocalDateTime.now(), List.of()
  );

  @BeforeEach
  public void setUp() {
    domainEventsBus = new SimpleDomainEventsBus();
    training.setTrainingSet(new TrainingSet(List.of()));
    when(transactionTemplate.execute(any()))
      .thenAnswer(invocation ->
        invocation.<TransactionCallback<Boolean>>getArgument(0)
          .doInTransaction(mock(TransactionStatus.class))
      );
    wordProgressUpdater = new WordProgressUpdater(
      wordProgressRepository, trainingProvider, domainEventsBus, transactionTemplate
    );
  }

  @Test
  public void testAllNewProgresses() {
    TrainingSet trainingSet = training.getTrainingSet();
    trainingSet.setFailed(List.of(word1));
    trainingSet.setApproved(List.of(word2, word3));

    when(trainingProvider.getTraining(any())).thenReturn(training);
    when(wordProgressRepository.findAllByOrigin(anyList())).thenReturn(List.of());

    domainEventsBus.publish(new TrainingDomainEvent(EMPTY_STRING, null));

    ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).update(updatesCaptor.capture());
    assertEquals(0, updatesCaptor.getValue().size());

    ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).create(createsCaptor.capture());
    assertEquals(3, createsCaptor.getValue().size());

    createsCaptor.getValue().sort(Comparator.comparing(WordProgress::getOrigin));
    WordProgress word1Progress = createsCaptor.getValue().get(0);
    assertEquals(word1, word1Progress.getOrigin());
    assertEquals(1, word1Progress.getFailed());
    assertEquals(0, word1Progress.getApproved());

    WordProgress word2Progress = createsCaptor.getValue().get(1);
    assertEquals(word2, word2Progress.getOrigin());
    assertEquals(0, word2Progress.getFailed());
    assertEquals(1, word2Progress.getApproved());

    WordProgress word3Progress = createsCaptor.getValue().get(2);
    assertEquals(word3, word3Progress.getOrigin());
    assertEquals(0, word2Progress.getFailed());
    assertEquals(1, word2Progress.getApproved());
  }

  @Test
  public void testAllExistingProgresses() {
    TrainingSet trainingSet = training.getTrainingSet();
    trainingSet.setFailed(List.of(word1));
    trainingSet.setApproved(List.of(word2, word3));
    WordProgress wordProgress1 = new WordProgress(word1, 10, 10, LocalDateTime.MIN);
    WordProgress wordProgress2 = new WordProgress(word2, 10, 10, LocalDateTime.MIN);
    WordProgress wordProgress3 = new WordProgress(word3, 10, 10, LocalDateTime.MIN);
    when(wordProgressRepository.findAllByOrigin(anyList())).
      thenReturn(List.of(wordProgress1, wordProgress2, wordProgress3));
    when(trainingProvider.getTraining(any())).thenReturn(training);

    domainEventsBus.publish(new TrainingDomainEvent(EMPTY_STRING, null));

    ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).update(updatesCaptor.capture());
    assertEquals(3, updatesCaptor.getValue().size());

    ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).create(createsCaptor.capture());
    assertEquals(0, createsCaptor.getValue().size());

    updatesCaptor.getValue().sort(Comparator.comparing(WordProgress::getOrigin));
    WordProgress word1Progress = updatesCaptor.getValue().get(0);
    assertEquals(word1, word1Progress.getOrigin());
    assertEquals(11, word1Progress.getFailed());
    assertEquals(10, word1Progress.getApproved());

    WordProgress word2Progress = updatesCaptor.getValue().get(1);
    assertEquals(word2, word2Progress.getOrigin());
    assertEquals(10, word2Progress.getFailed());
    assertEquals(11, word2Progress.getApproved());

    WordProgress word3Progress = updatesCaptor.getValue().get(2);
    assertEquals(word3, word3Progress.getOrigin());
    assertEquals(10, word2Progress.getFailed());
    assertEquals(11, word2Progress.getApproved());
  }

  @Test
  public void testEmptySession() {
    TrainingSet trainingSet = training.getTrainingSet();
    trainingSet.setFailed(List.of());
    trainingSet.setApproved(List.of());
    when(wordProgressRepository.findAllByOrigin(anyList())).thenReturn(List.of());
    when(trainingProvider.getTraining(any())).thenReturn(training);

    domainEventsBus.publish(new TrainingDomainEvent(EMPTY_STRING, null));

    ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).update(updatesCaptor.capture());
    assertEquals(0, updatesCaptor.getValue().size());

    ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
    verify(wordProgressRepository).create(createsCaptor.capture());
    assertEquals(0, createsCaptor.getValue().size());
  }
}
