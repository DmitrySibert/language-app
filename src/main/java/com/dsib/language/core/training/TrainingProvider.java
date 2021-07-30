package com.dsib.language.core.training;

import com.dsib.language.core.dictionary.DictionaryService;
import com.dsib.language.core.word.RepeatWordEntity;
import com.dsib.language.core.word.RepeatWordRepository;
import com.dsib.language.core.word.Word;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class TrainingProvider {

    private final DictionaryService dictionaryService;
    private final RepeatWordRepository repeatWordRepository;

    public TrainingProvider(DictionaryService dictionaryService, RepeatWordRepository repeatWordRepository) {
        this.dictionaryService = dictionaryService;
        this.repeatWordRepository = repeatWordRepository;
    }

    public Training getTraining(TrainingType type, List<String> tags) {
        if (TrainingType.REPEAT.equals(type)) {
            return getSimpleRepeatTraining();
        }
        if (TrainingType.TAGGED.equals(type)) {
            return getSimpleTrainingByTags(tags);
        }

        return null;
    }

    public Training getSimpleTrainingByTags(List<String> tags) {
        List<Word> trainingWords = dictionaryService.getWordsByTags(tags);
        Collections.shuffle(trainingWords);
        return new Training(trainingWords);
    }

    public Training getSimpleRepeatTraining() {
        List<String> origins = new LinkedList<>();
        for (RepeatWordEntity repeatWordEntity: repeatWordRepository.findAll()) {
            origins.add(repeatWordEntity.getOrigin());
        }
        List<Word> trainingWords = dictionaryService.getWordsByOrigin(origins);
        Collections.shuffle(trainingWords);
        return new Training(trainingWords);
    }
}
