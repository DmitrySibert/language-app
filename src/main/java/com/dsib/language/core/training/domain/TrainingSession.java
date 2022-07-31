package com.dsib.language.core.training.domain;

import com.dsib.language.core.word.domain.Word;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class TrainingSession {

    @Getter @Setter
    private List<Word> words;
    @Getter @Setter
    private List<Word> wordsForRepetition;
    @Getter @Setter
    private List<Word> approvedWords;

    public TrainingSession(List<Word> words) {
        this.words = new ArrayList<>(words);
        wordsForRepetition = new LinkedList<>();
        approvedWords = new LinkedList<>();
    }

    public void shuffle() {
        Collections.shuffle(this.words);
    }
}
