package com.dsib.language.core.training;

import com.dsib.language.core.progress.domain.ProgressType;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressFailProneService;
import com.dsib.language.core.training.application.TrainingProvider;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingRepository;
import com.dsib.language.core.training.domain.TrainingStatus;
import com.dsib.language.core.training.domain.TrainingType;
import com.dsib.language.core.word.application.WordService;
import com.dsib.language.core.word.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.dsib.language.core.TestUtils.ANY_STRING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingProviderTest {

  @Mock
  WordProgressFailProneService wordProgressFailProneService;
  @Mock
  private WordService wordService;
  @Mock
  private TrainingRepository trainingRepository;

  private TrainingProvider trainingProvider;

  private final String OWNER_ID = "ownerId";
  private final Word word1 = new Word(
    "word1", "translate1", List.of("info1", "info2"), List.of("tag1", "tag2")
  );
  private final Word word2 = new Word(
    "word2", "translate2", List.of("info1", "info2"), List.of("tag1", "tag2")
  );
  private final WordProgress wordProgress1 = new WordProgress("word1", "ownerId");
  private final WordProgress wordProgress2 = new WordProgress("word2", "ownerId");

  @BeforeEach
  public void setUp() {
    trainingProvider = new TrainingProvider(trainingRepository, wordService, wordProgressFailProneService);
  }

  @Test
  public void testGetTrainingByTags() {
    when(wordService.getByTags(any(List.class), any(), any()))
      .then(args -> {
        List<Word> words = new LinkedList<>();
        words.add(word1);
        words.add(word2);
        return words;
      });
    when(trainingRepository.create(any(Training.class)))
      .then(args -> args.getArgument(0));

    Training training = trainingProvider.createNewTraining(TrainingType.TAGGED, List.of("tag1"), null, OWNER_ID);

    assertEquals(2, training.getTrainingSet().getWords().size());
    assertEquals(2, training.getSize());
    assertEquals(TrainingStatus.CREATED, training.getStatus());
    assertEquals(List.of("tag1"), training.getTags());
    assertEquals(TrainingType.TAGGED, training.getType());
    assertEquals(OWNER_ID, training.getOwnerId());
    assertNotNull(training.getId());
    assertNotNull(training.getCreatedAt());
    assertNull(training.getCompletedAt());
  }

  @Test
  public void testGetSimpleRepeatTraining() {
    when(wordProgressFailProneService.getByType(eq(ProgressType.MOST_FAILED), anyInt(), anyString()))
      .thenReturn(List.of(wordProgress1, wordProgress2));
    when(trainingRepository.create(any(Training.class)))
      .then(args -> args.getArgument(0));

    Training training = trainingProvider.createNewTraining(TrainingType.REPEAT, null, 2, OWNER_ID);

    assertEquals(2, training.getTrainingSet().getWords().size());
    assertEquals(2, training.getSize());
    assertEquals(TrainingStatus.CREATED, training.getStatus());
    assertEquals(0, training.getTags().size());
    assertEquals(TrainingType.REPEAT, training.getType());
    assertEquals(OWNER_ID, training.getOwnerId());
    assertNotNull(training.getId());
    assertNotNull(training.getCreatedAt());
    assertNull(training.getCompletedAt());
  }

  @Test
  public void testGetSimpleRepeatTraining_ActualSizeIsLess() {
    when(wordProgressFailProneService.getByType(eq(ProgressType.MOST_FAILED), anyInt(), anyString()))
      .thenReturn(List.of(wordProgress1, wordProgress2));
    when(trainingRepository.create(any(Training.class)))
      .then(args -> args.getArgument(0));

    Training training = trainingProvider.createNewTraining(TrainingType.REPEAT, null, 10, OWNER_ID);

    assertEquals(2, training.getTrainingSet().getWords().size());
    assertEquals(2, training.getSize());
    assertEquals(TrainingStatus.CREATED, training.getStatus());
    assertEquals(0, training.getTags().size());
    assertEquals(TrainingType.REPEAT, training.getType());
    assertEquals(OWNER_ID, training.getOwnerId());
    assertNotNull(training.getId());
    assertNotNull(training.getCreatedAt());
    assertNull(training.getCompletedAt());
  }

  @Test
  public void testGetSimpleRandomTraining() {
    when(wordService.getRandom(any(Integer.class), any()))
      .thenReturn(Arrays.asList(word1, word2));
    when(trainingRepository.create(any(Training.class)))
      .then(args -> args.getArgument(0));

    Training training = trainingProvider.createNewTraining(TrainingType.RANDOM, null, 2, OWNER_ID);

    assertEquals(2, training.getTrainingSet().getWords().size());
    assertEquals(2, training.getSize());
    assertEquals(TrainingStatus.CREATED, training.getStatus());
    assertEquals(0, training.getTags().size());
    assertEquals(TrainingType.RANDOM, training.getType());
    assertEquals(OWNER_ID, training.getOwnerId());
    assertNotNull(training.getId());
    assertNotNull(training.getCreatedAt());
    assertNull(training.getCompletedAt());
  }

  @Test
  public void testGetSimpleRandomTraining_ActualSizeIsLess() {
    when(wordService.getRandom(any(Integer.class), any()))
      .thenReturn(Arrays.asList(word1, word2));
    when(trainingRepository.create(any(Training.class)))
      .then(args -> args.getArgument(0));

    Training training = trainingProvider.createNewTraining(TrainingType.RANDOM, null, 10, OWNER_ID);

    assertEquals(2, training.getTrainingSet().getWords().size());
    assertEquals(2, training.getSize());
    assertEquals(TrainingStatus.CREATED, training.getStatus());
    assertEquals(0, training.getTags().size());
    assertEquals(TrainingType.RANDOM, training.getType());
    assertEquals(OWNER_ID, training.getOwnerId());
    assertNotNull(training.getId());
    assertNotNull(training.getCreatedAt());
    assertNull(training.getCompletedAt());
  }
}
