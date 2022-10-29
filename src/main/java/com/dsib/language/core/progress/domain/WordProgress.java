package com.dsib.language.core.progress.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class WordProgress {

    private @Getter final String origin;
    private @Getter final String ownerId;
    private @Getter int approved;
    private @Getter int failed;
    private @Getter LocalDateTime updatedAt;

    public WordProgress(String origin, String ownerId) {
      this(origin, ownerId, 0, 0, LocalDateTime.now());
    }

    public void addApprove() {
        ++approved;
        updatedAt = LocalDateTime.now();
    }

    public void addFail() {
        ++failed;
        updatedAt = LocalDateTime.now();
    }
}
