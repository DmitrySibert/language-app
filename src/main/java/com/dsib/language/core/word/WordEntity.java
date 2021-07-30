package com.dsib.language.core.word;

import org.springframework.data.annotation.Id;

public class WordEntity {

    @Id
    private String origin;
    private String translate;
    private String wordInfo;
    private String tags;

    public WordEntity(String origin, String translate, String wordInfo, String tags) {
        this.origin = origin;
        this.translate = translate;
        this.wordInfo = wordInfo;
        this.tags = tags;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getWordInfo() {
        return wordInfo;
    }

    public void setWordInfo(String wordInfo) {
        this.wordInfo = wordInfo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
