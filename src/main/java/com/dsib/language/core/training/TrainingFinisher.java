package com.dsib.language.core.training;

import com.dsib.language.core.word.RepeatWordEntity;
import com.dsib.language.core.word.RepeatWordRepository;
import com.dsib.language.core.word.Word;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingFinisher {

    private RepeatWordRepository repeatWordRepository;

    public TrainingFinisher(RepeatWordRepository repeatWordRepository) {
        this.repeatWordRepository = repeatWordRepository;
    }

    public void finishTrainingSession(Training training) {
        List<String> wordsForRepetition = training.getWordsForRepetition().stream()
                .map(Word::getWordOrigin)
                .collect(Collectors.toList());
        List<String> currentWordsForRepetition = new LinkedList<>();
        repeatWordRepository.findAll().forEach(repeatWordEntity -> currentWordsForRepetition.add(repeatWordEntity.getOrigin()));
        wordsForRepetition.removeAll(currentWordsForRepetition);
        if (!wordsForRepetition.isEmpty()) {
            repeatWordRepository.saveAll(wordsForRepetition.stream().map(RepeatWordEntity::new).collect(Collectors.toList()));
        }
        if (!training.getApprovedWords().isEmpty()) {
            repeatWordRepository.deleteAllById(training.getWordsForRepetition().stream()
                    .map(Word::getWordOrigin)
                    .collect(Collectors.toList())
            );
        }
    }
}
