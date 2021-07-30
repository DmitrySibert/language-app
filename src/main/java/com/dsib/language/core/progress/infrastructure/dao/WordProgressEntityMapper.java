package com.dsib.language.core.progress.infrastructure.dao;

import com.dsib.language.core.progress.domain.WordProgress;

public class WordProgressEntityMapper {

    public WordProgressEntity toEntityNew(WordProgress wordProgress) {
        return new WordProgressEntity(
                wordProgress.getOrigin(), wordProgress.getApproved(),
                wordProgress.getFailed(), wordProgress.getUpdatedAt()
        );
    }

    public WordProgressEntity toEntityExisting(WordProgress wordProgress) {
        return new WordProgressEntity(
                wordProgress.getOrigin(), wordProgress.getApproved(),
                wordProgress.getFailed(), wordProgress.getUpdatedAt(),
                false
        );
    }

    public WordProgress fromEntity(WordProgressEntity entity) {
        return new WordProgress(entity.getOrigin(), entity.getApproved(), entity.getFailed(), entity.getUpdatedAt());
    }
}
