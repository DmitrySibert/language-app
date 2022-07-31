package com.dsib.language.core.training.infrastructure.dao;

import com.dsib.language.core.training.domain.TrainingStatus;
import com.dsib.language.core.training.domain.TrainingType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table("training") @Data
public class TrainingEntity implements Persistable<String> {

    @Transient
    private final boolean isNew;
    @Id
    private String id;
    private TrainingStatus status;
    private TrainingType type;
    private Integer size;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public TrainingEntity() {
        this.isNew = true;
    }

    public TrainingEntity(
            String id, TrainingStatus status, TrainingType type, Integer size, List<String> tags,
            LocalDateTime createdAt, LocalDateTime completedAt, boolean isNew
    ) {
        this.isNew = isNew;
        this.id = id;
        this.status = status;
        this.type = type;
        this.size = size;
        this.tags = tags;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }
}
