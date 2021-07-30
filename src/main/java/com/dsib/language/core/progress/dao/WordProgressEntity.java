package com.dsib.language.core.progress.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("word_progress")
@Data
public class WordProgressEntity implements Persistable<String> {

    @Transient
    private boolean isNew;
    @Id
    private String origin;
    private int approved;
    private int failed;
    private LocalDateTime updatedAt;

    public WordProgressEntity() {
    }

    public WordProgressEntity(String origin, int approved, int failed, LocalDateTime updatedAt) {
        isNew = true;
        this.origin = origin;
        this.approved = approved;
        this.failed = failed;
        this.updatedAt = updatedAt;
    }

    public WordProgressEntity(String origin, int approved, int failed, LocalDateTime updatedAt, boolean isNew) {
        this.isNew = isNew;
        this.origin = origin;
        this.approved = approved;
        this.failed = failed;
        this.updatedAt = updatedAt;
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
