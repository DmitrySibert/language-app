package com.dsib.language.core.progress.domain;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WordProgressFailProneService {

    private final WordProgressRepository wordProgressRepository;

    public WordProgressFailProneService(WordProgressRepository wordProgressRepository) {
        this.wordProgressRepository = wordProgressRepository;
    }

    public List<WordProgress> getByType(ProgressType progressType, int number) {
        List<WordProgress> wordsProgress = new LinkedList<>();
        switch (progressType) {
            case MOST_FAILED: {
                wordProgressRepository.findMostFailedNonApproved(number).forEach(wordsProgress::add);
            }
            case OLD: {
                wordProgressRepository.findOldNonApproved(number).forEach(wordsProgress::add);
            }
        }

        return wordsProgress;
    }
}
