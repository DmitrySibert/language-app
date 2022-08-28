package com.dsib.language.core.training.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class TrainingSet {

    @Getter @Setter
    private List<String> words;
    @Getter @Setter
    private List<String> failed;
    @Getter @Setter
    private List<String> approved;

    public TrainingSet(List<String> words) {
      this(words, List.of(), List.of());
    }

    public TrainingSet(List<String> words, List<String> failed, List<String> approvedWords) {
      this.words = new ArrayList<>(words);
      this.failed = new ArrayList<>(failed);
      this.approved = new ArrayList<>(approvedWords);
    }

    public void shuffle() {
      Collections.shuffle(this.words);
    }
}
