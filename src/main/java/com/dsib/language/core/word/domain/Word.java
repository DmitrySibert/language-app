package com.dsib.language.core.word.domain;

import lombok.Getter;

import java.util.List;

public class Word {

    @Getter
    private String wordOrigin;
    @Getter
    private String wordTranslate;
    @Getter
    private List<String> wordInfo;
    @Getter
    private List<String> tags;

    public Word(String wordOrigin, String wordTranslate, List<String> wordInfo, List<String> tags) {
        this.wordTranslate = wordTranslate;
        this.wordOrigin = wordOrigin;
        this.wordInfo = wordInfo != null ? wordInfo: List.of();
        this.tags = tags != null ? tags: List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        return wordOrigin.equals(word.wordOrigin);
    }

    @Override
    public int hashCode() {
        return wordOrigin.hashCode();
    }
}
