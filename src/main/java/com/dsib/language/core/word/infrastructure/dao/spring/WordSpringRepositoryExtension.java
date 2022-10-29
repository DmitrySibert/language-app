package com.dsib.language.core.word.infrastructure.dao.spring;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;

import java.util.List;

public interface WordSpringRepositoryExtension {
    int[] upsert(List<WordEntity> words);
    List<WordEntity> findByTags(List<String> tags, String ownerId);
}
