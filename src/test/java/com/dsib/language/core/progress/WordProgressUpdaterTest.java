package com.dsib.language.core.progress;

import com.dsib.language.core.infrastructure.DomainEventsBus;
import com.dsib.language.core.infrastructure.SimpleDomainEventsBus;
import com.dsib.language.core.training.*;
import com.dsib.language.core.word.Word;
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
    private DomainEventsBus domainEventsBus;
    @Mock
    private TransactionTemplate transactionTemplate;

    private WordProgressUpdater wordProgressUpdater;

    private final Word word1 = new Word("word1", EMPTY_STRING, List.of(), List.of());
    private final Word word2 = new Word("word2", EMPTY_STRING, List.of(), List.of());
    private final Word word3 = new Word("word3", EMPTY_STRING, List.of(), List.of());
    private final Training training = new Training(
            "id", TrainingStatus.COMPLETED, TrainingType.RANDOM, Integer.MAX_VALUE, List.of(), LocalDateTime.now()
    );

    @BeforeEach
    public void setUp() {
        domainEventsBus = new SimpleDomainEventsBus();
        when(transactionTemplate.execute(any()))
                .thenAnswer(invocation ->
                    invocation.<TransactionCallback<Boolean>>getArgument(0)
                        .doInTransaction(mock(TransactionStatus.class))
                );
        wordProgressUpdater = new WordProgressUpdater(wordProgressRepository, domainEventsBus, transactionTemplate);
    }

    @Test
    public void testAllNewProgresses() {
        TrainingSession trainingSession = new TrainingSession(List.of(word1, word2, word3));
        trainingSession.setWordsForRepetition(List.of(word1));
        trainingSession.setApprovedWords(List.of(word2, word3));
        training.setTrainingSession(trainingSession);
        TrainingDomainEvent trainingDomainEvent = new TrainingDomainEvent(training);
        when(wordProgressRepository.findAllByOrigin(anyList())).thenReturn(List.of());

        domainEventsBus.publish(trainingDomainEvent);

        ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).update(updatesCaptor.capture());
        assertEquals(0, updatesCaptor.getValue().size());

        ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).create(createsCaptor.capture());
        assertEquals(3, createsCaptor.getValue().size());

        createsCaptor.getValue().sort(Comparator.comparing(WordProgress::getOrigin));
        WordProgress word1Progress = createsCaptor.getValue().get(0);
        assertEquals(word1.getWordOrigin(), word1Progress.getOrigin());
        assertEquals(1, word1Progress.getFailed());
        assertEquals(0, word1Progress.getApproved());

        WordProgress word2Progress = createsCaptor.getValue().get(1);
        assertEquals(word2.getWordOrigin(), word2Progress.getOrigin());
        assertEquals(0, word2Progress.getFailed());
        assertEquals(1, word2Progress.getApproved());

        WordProgress word3Progress = createsCaptor.getValue().get(2);
        assertEquals(word3.getWordOrigin(), word3Progress.getOrigin());
        assertEquals(0, word2Progress.getFailed());
        assertEquals(1, word2Progress.getApproved());
    }

    @Test
    public void testAllExistingProgresses() {
        TrainingSession trainingSession = new TrainingSession(List.of(word1, word2, word3));
        trainingSession.setWordsForRepetition(List.of(word1));
        trainingSession.setApprovedWords(List.of(word2, word3));
        training.setTrainingSession(trainingSession);
        TrainingDomainEvent trainingDomainEvent = new TrainingDomainEvent(training);
        WordProgress wordProgress1 = new WordProgress(word1.getWordOrigin(), 10, 10, LocalDateTime.MIN);
        WordProgress wordProgress2 = new WordProgress(word2.getWordOrigin(), 10, 10, LocalDateTime.MIN);
        WordProgress wordProgress3 = new WordProgress(word3.getWordOrigin(), 10, 10, LocalDateTime.MIN);
        when(wordProgressRepository.findAllByOrigin(anyList())).
                thenReturn(List.of(wordProgress1, wordProgress2, wordProgress3));

        domainEventsBus.publish(trainingDomainEvent);

        ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).update(updatesCaptor.capture());
        assertEquals(3, updatesCaptor.getValue().size());

        ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).create(createsCaptor.capture());
        assertEquals(0, createsCaptor.getValue().size());

        updatesCaptor.getValue().sort(Comparator.comparing(WordProgress::getOrigin));
        WordProgress word1Progress = updatesCaptor.getValue().get(0);
        assertEquals(word1.getWordOrigin(), word1Progress.getOrigin());
        assertEquals(11, word1Progress.getFailed());
        assertEquals(10, word1Progress.getApproved());

        WordProgress word2Progress = updatesCaptor.getValue().get(1);
        assertEquals(word2.getWordOrigin(), word2Progress.getOrigin());
        assertEquals(10, word2Progress.getFailed());
        assertEquals(11, word2Progress.getApproved());

        WordProgress word3Progress = updatesCaptor.getValue().get(2);
        assertEquals(word3.getWordOrigin(), word3Progress.getOrigin());
        assertEquals(10, word2Progress.getFailed());
        assertEquals(11, word2Progress.getApproved());
    }

    @Test
    public void testEmptySession() {
        TrainingSession trainingSession = new TrainingSession(List.of(word1, word2, word3));
        trainingSession.setWordsForRepetition(List.of());
        trainingSession.setApprovedWords(List.of());
        training.setTrainingSession(trainingSession);
        TrainingDomainEvent trainingDomainEvent = new TrainingDomainEvent(training);
        when(wordProgressRepository.findAllByOrigin(anyList())).thenReturn(List.of());

        domainEventsBus.publish(trainingDomainEvent);

        ArgumentCaptor<List<WordProgress>> updatesCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).update(updatesCaptor.capture());
        assertEquals(0, updatesCaptor.getValue().size());

        ArgumentCaptor<List<WordProgress>> createsCaptor = ArgumentCaptor.forClass(List.class);
        verify(wordProgressRepository).create(createsCaptor.capture());
        assertEquals(0, createsCaptor.getValue().size());
    }
}
