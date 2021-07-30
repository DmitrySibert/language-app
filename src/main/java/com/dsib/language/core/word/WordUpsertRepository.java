package com.dsib.language.core.word;

import java.util.List;

public interface WordUpsertRepository {
    int[] upsert(List<WordEntity> words);
}
