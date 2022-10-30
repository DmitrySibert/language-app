package com.dsib.language.core.training.application;

import com.dsib.language.core.progress.domain.ProgressType;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressFailProneService;
import com.dsib.language.core.training.domain.*;
import com.dsib.language.core.word.domain.Word;
import com.dsib.language.core.word.application.WordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainingProvider {

  private final TrainingRepository trainingRepository;
  private final WordService wordService;
  private final WordProgressFailProneService wordProgressFailProneService;

  public TrainingProvider(
    TrainingRepository trainingRepository, WordService wordService,
    WordProgressFailProneService wordProgressFailProneService
  ) {
    this.trainingRepository = trainingRepository;
    this.wordService = wordService;
    this.wordProgressFailProneService = wordProgressFailProneService;
  }

  public Training getTraining(String id) {
    return trainingRepository.find(id)
      .orElseThrow(() -> new NoSuchElementException("" + id));
  }

  public Training createNewTraining(TrainingType type, List<String> tags, Integer size, String ownerId) {
    Training training;
    switch (type) {
      case RANDOM: {
        List<Word> trainingWords = wordService.getRandom(size, ownerId);
        training = new Training(
          UUID.randomUUID().toString(), ownerId, TrainingStatus.CREATED, TrainingType.RANDOM,
          trainingWords.size(), List.of(), LocalDateTime.now(), toString(trainingWords)
        );
        break;
      }
      case REPEAT: {
        List<String> origins = wordProgressFailProneService.getByType(ProgressType.MOST_FAILED, size, ownerId)
          .parallelStream()
          .map(WordProgress::getOrigin)
          .collect(Collectors.toList());
        training = new Training(
          UUID.randomUUID().toString(), ownerId, TrainingStatus.CREATED, TrainingType.REPEAT,
          origins.size(), List.of(), LocalDateTime.now(), origins
        );
        break;
      }
      case TAGGED: {
        List<Word> trainingWords = wordService.getByTags(tags, size, ownerId);
        training = new Training(
          UUID.randomUUID().toString(), ownerId, TrainingStatus.CREATED, TrainingType.TAGGED,
          trainingWords.size(), tags, LocalDateTime.now(), toString(trainingWords)
        );
        break;
      }
      default: {
        throw new IllegalArgumentException("Type " + type + " is not supported");
      }
    }
    return trainingRepository.create(training);
  }

  private List<String> toString(List<Word> words) {
    return words.stream()
      .map(Word::getWordOrigin)
      .collect(Collectors.toList());
  }
}
