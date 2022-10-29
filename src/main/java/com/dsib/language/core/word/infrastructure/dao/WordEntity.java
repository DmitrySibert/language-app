package com.dsib.language.core.word.infrastructure.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.List;

public class WordEntity implements Persistable<String> {

    @Transient
    private final boolean isNew;
    @Id
    private String origin;
    private WordData data;
    private String ownerId;

    public WordEntity() {
        isNew = true;
    }

    public WordEntity(String origin, String ownerId, WordData data, boolean isNew) {
        this.origin = origin;
        this.ownerId = ownerId;
        this.data = data;
        this.isNew = isNew;
    }

    public WordEntity(
      String origin, String ownerId, String translate, List<String> wordInfo, List<String> tags, boolean isNew
    ) {
      this(origin, ownerId, new WordData(translate, wordInfo, tags), isNew);
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public WordData getData() {
        return data;
    }

    public void setData(WordData data) {
        this.data = data;
    }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Override
    public String getId() {
        return origin;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
