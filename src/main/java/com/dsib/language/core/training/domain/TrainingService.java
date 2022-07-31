package com.dsib.language.core.training.domain;

import com.dsib.language.core.progress.domain.ProgressType;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressFailProneService;
import com.dsib.language.core.word.domain.WordService;
import com.dsib.language.core.word.domain.Word;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//TODO: here should be the domain logic:
//      2 thinking about communication with WordService. Who should do it application or domain?
//      3 how to read an additional info for use-cases? should application do it instead?
//      4 call other contexts with using of an application layer
public class TrainingService {

    private final WordService wordService;
    private final WordProgressFailProneService wordProgressFailProneService;

    public TrainingService(
            WordService wordService, WordProgressFailProneService wordProgressFailProneService
    ) {
        this.wordService = wordService;
        this.wordProgressFailProneService = wordProgressFailProneService;
    }

    public Training getSimpleTrainingByTags(List<String> tags) {
        List<Word> trainingWords = wordService.getByTags(tags);
        Training training = new Training(
                UUID.randomUUID().toString(), TrainingStatus.CREATED, TrainingType.TAGGED,
                trainingWords.size(), tags, LocalDateTime.now()
        );
        TrainingSession session = training.getTrainingSession();
        session.setWords(trainingWords);
        session.shuffle();
        return training;
    }

    public Training getSimpleRandomTraining(int size) {
        List<Word> trainingWords = wordService.getAll();
        Collections.shuffle(trainingWords);
        int actualSize = trainingWords.size() < size ? trainingWords.size() : size;
        Training training = new Training(
                UUID.randomUUID().toString(), TrainingStatus.CREATED, TrainingType.RANDOM,
                actualSize, List.of(), LocalDateTime.now()
        );
        TrainingSession session = training.getTrainingSession();
        session.setWords(trainingWords.subList(0, actualSize));
        return training;
    }

    public Training getSimpleRepeatTraining(int size) {
        List<String> origins = wordProgressFailProneService.getByType(ProgressType.MOST_FAILED, size).parallelStream()
                .map(WordProgress::getOrigin)
                .collect(Collectors.toList());
        List<Word> trainingWords = wordService.getByOrigin(origins);
        Collections.shuffle(trainingWords);
        Training training = new Training(
                UUID.randomUUID().toString(), TrainingStatus.CREATED, TrainingType.REPEAT,
                trainingWords.size(), List.of(), LocalDateTime.now()
        );
        TrainingSession session = training.getTrainingSession();
        session.setWords(trainingWords);
        return training;
    }
}
