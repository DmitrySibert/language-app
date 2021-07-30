package com.dsib.language.core.training;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static com.dsib.language.core.util.AssertUtils.assertNotNull;

public class Training {

    @Getter
    private String id;
    @Getter
    private TrainingStatus status;
    @Getter
    private final TrainingType type;
    @Getter
    private final Integer size;
    @Getter
    private final List<String> tags;
    @Getter
    private final LocalDateTime createdAt;
    @Getter
    private LocalDateTime completedAt;

    /**
     * <c>TrainingSession</c> isn't persisted. It is enriched only by demand.
     */
    @Getter @Setter
    private TrainingSession trainingSession;

    public Training(
            String id, TrainingStatus status, TrainingType type,
            Integer size, List<String> tags, LocalDateTime createdAt
    ) {
        assertNotNull("id", id);
        assertNotNull("status", status);
        assertNotNull("type", type);
        assertNotNull("tags", tags);
        assertNotNull("createdAt", createdAt);
        this.id = id;
        this.status = status;
        this.type = type;
        this.size = size;
        this.tags = tags;
        this.createdAt = createdAt;
        trainingSession = new TrainingSession(List.of());
        completedAt = null;
    }

    public TrainingDomainEvent complete() {
        setStatus(TrainingStatus.COMPLETED);
        setCompletedAt(LocalDateTime.now());

        return new TrainingDomainEvent(this);
    }

    public void setStatus(TrainingStatus status) {
        validateStatusChange();
        assertNotNull("status", status);
        this.status = status;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        assertNotNull("completedAt", completedAt);
        this.completedAt = completedAt;
    }

    public void setId(String id) {
        if (this.id != null) {
            throw new IllegalStateException("Id can't be overwrited");
        }
        this.id = id;
    }

    private void validateStatusChange() {
        if (TrainingStatus.COMPLETED.equals(status)) {
            throw new IllegalStateException("Can't change status from " + TrainingStatus.COMPLETED + " to " + status);
        }
    }
}
