package com.dsib.language.core.progress.domain;

import lombok.Getter;

import java.time.LocalDateTime;

public class WordProgress {

    private @Getter String origin;
    private @Getter int approved;
    private @Getter int failed;
    private @Getter LocalDateTime updatedAt;

    public WordProgress(String origin, int approved, int failed, LocalDateTime updatedAt) {
        this.origin = origin;
        this.approved = approved;
        this.failed = failed;
        this.updatedAt = updatedAt;
    }

    public WordProgress(String origin) {
        this.origin = origin;
        this.approved = 0;
        this.failed = 0;
        this.updatedAt = LocalDateTime.now();
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
