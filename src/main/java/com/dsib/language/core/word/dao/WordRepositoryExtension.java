package com.dsib.language.core.word.dao;

import java.util.List;

public interface WordRepositoryExtension {
    int[] upsert(List<WordEntity> words);
    List<WordEntity> findByTags(List<String> tags);
}
