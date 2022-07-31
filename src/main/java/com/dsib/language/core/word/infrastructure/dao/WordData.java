package com.dsib.language.core.word.infrastructure.dao;

import java.util.List;

public class WordData {
    private String translate;
    private List<String> wordInfo;
    private List<String> tags;

    public WordData() {
    }

    public WordData(String translate, List<String> wordInfo, List<String> tags) {
        this.translate = translate;
        this.wordInfo = wordInfo;
        this.tags = tags;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
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
}
