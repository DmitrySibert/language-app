package com.dsib.language.core.word;

import java.util.List;

public class Word {
    private String wordOrigin;
    private String wordTranslate;
    private List<String> wordInfo;
    private List<String> tags;

    public Word(String wordOrigin, String wordTranslate, String[] wordInfo, String[] tags) {
        this.wordTranslate = wordTranslate;
        this.wordOrigin = wordOrigin;
        this.wordInfo = List.of(wordInfo);
        this.tags = List.of(tags);
    }

    public String getWordTranslate() {
        return wordTranslate;
    }

    public void setWordTranslate(String wordTranslate) {
        this.wordTranslate = wordTranslate;
    }

    public String getWordOrigin() {
        return wordOrigin;
    }

    public void setWordOrigin(String wordOrigin) {
        this.wordOrigin = wordOrigin;
    }

    public List<String> getWordInfo() {
        return wordInfo;
    }

    public void setWordInfo(List<String> wordInfo) {
        this.wordInfo = wordInfo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
