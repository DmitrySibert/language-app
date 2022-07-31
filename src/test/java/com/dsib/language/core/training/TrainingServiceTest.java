package com.dsib.language.core.training;

import com.dsib.language.core.progress.domain.ProgressType;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressFailProneService;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingService;
import com.dsib.language.core.training.domain.TrainingStatus;
import com.dsib.language.core.training.domain.TrainingType;
import com.dsib.language.core.word.domain.WordService;
import com.dsib.language.core.word.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private WordService wordService;
    @Mock
    private WordProgressFailProneService wordProgressFailProneService;

    private final Word word1 = new Word(
            "word1", "translate1", List.of("info1", "info2"), List.of("tag1", "tag2")
    );
    private final Word word2 = new Word(
            "word2", "translate2", List.of("info1", "info2"), List.of("tag1", "tag2")
    );
    private final WordProgress wordProgress1 = new WordProgress("word1");
    private final WordProgress wordProgress2 = new WordProgress("word2");

    private TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        trainingService = new TrainingService(wordService, wordProgressFailProneService);
    }

    @Test
    public void testGetTrainingByTags() {
        when(wordService.getByTags(any(List.class)))
            .then(args -> {
                List<Word> words = new LinkedList<>();
                words.add(word1);
                words.add(word2);
                return words;
            });

        Training training = trainingService.getSimpleTrainingByTags(List.of("tag1"));

        assertEquals(2, training.getTrainingSession().getWords().size());
        assertEquals(2, training.getSize());
        assertEquals(TrainingStatus.CREATED, training.getStatus());
        assertEquals(List.of("tag1"), training.getTags());
        assertEquals(TrainingType.TAGGED, training.getType());
        assertNotNull(training.getId());
        assertNotNull(training.getCreatedAt());
        assertNull(training.getCompletedAt());
    }

    @Test
    public void testGetSimpleRepeatTraining() {
        when(wordProgressFailProneService.getByType(eq(ProgressType.MOST_FAILED), anyInt()))
            .thenReturn(List.of(wordProgress1, wordProgress2));
        when(wordService.getByOrigin(List.of(wordProgress1.getOrigin(), wordProgress2.getOrigin())))
            .then(args -> {
                List<Word> words = new LinkedList<>();
                words.add(word1);
                words.add(word2);
                return words;
            });

        Training training = trainingService.getSimpleRepeatTraining(2);

        assertEquals(2, training.getTrainingSession().getWords().size());
        assertEquals(2, training.getSize());
        assertEquals(TrainingStatus.CREATED, training.getStatus());
        assertEquals(0, training.getTags().size());
        assertEquals(TrainingType.REPEAT, training.getType());
        assertNotNull(training.getId());
        assertNotNull(training.getCreatedAt());
        assertNull(training.getCompletedAt());
    }

    @Test
    public void testGetSimpleRepeatTraining_ActualSizeIsLess() {
        when(wordProgressFailProneService.getByType(eq(ProgressType.MOST_FAILED), anyInt()))
                .thenReturn(List.of(wordProgress1, wordProgress2));
        when(wordService.getByOrigin(List.of(wordProgress1.getOrigin(), wordProgress2.getOrigin())))
                .then(args -> {
                    List<Word> words = new LinkedList<>();
                    words.add(word1);
                    words.add(word2);
                    return words;
                });

        Training training = trainingService.getSimpleRepeatTraining(10);

        assertEquals(2, training.getTrainingSession().getWords().size());
        assertEquals(2, training.getSize());
        assertEquals(TrainingStatus.CREATED, training.getStatus());
        assertEquals(0, training.getTags().size());
        assertEquals(TrainingType.REPEAT, training.getType());
        assertNotNull(training.getId());
        assertNotNull(training.getCreatedAt());
        assertNull(training.getCompletedAt());
    }


    @Test
    public void testGetSimpleRandomTraining() {
        when(wordService.getAll())
            .thenReturn(Arrays.asList(word1, word2));

        Training training = trainingService.getSimpleRandomTraining(2);

        assertEquals(2, training.getTrainingSession().getWords().size());
        assertEquals(2, training.getSize());
        assertEquals(TrainingStatus.CREATED, training.getStatus());
        assertEquals(0, training.getTags().size());
        assertEquals(TrainingType.RANDOM, training.getType());
        assertNotNull(training.getId());
        assertNotNull(training.getCreatedAt());
        assertNull(training.getCompletedAt());
    }


    @Test
    public void testGetSimpleRandomTraining_ActualSizeIsLess() {
        when(wordService.getAll())
            .thenReturn(Arrays.asList(word1, word2));

        Training training = trainingService.getSimpleRandomTraining(10);

        assertEquals(2, training.getTrainingSession().getWords().size());
        assertEquals(2, training.getSize());
        assertEquals(TrainingStatus.CREATED, training.getStatus());
        assertEquals(0, training.getTags().size());
        assertEquals(TrainingType.RANDOM, training.getType());
        assertNotNull(training.getId());
        assertNotNull(training.getCreatedAt());
        assertNull(training.getCompletedAt());
    }
}
