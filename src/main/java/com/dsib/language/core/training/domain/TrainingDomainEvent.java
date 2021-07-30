package com.dsib.language.core.training.domain;

import com.dsib.language.core.common.event.DomainEvent;
import lombok.Getter;

import static com.dsib.language.core.common.util.AssertUtils.assertNotNull;

public class TrainingDomainEvent extends DomainEvent {

    private static final String NAME = "TrainingDomainEvent";

    @Getter
    private final String entityId;
    @Getter
    private final TrainingStatus status;

    public TrainingDomainEvent(String entityId, TrainingStatus status) {
        super(NAME);
        assertNotNull("entityId", entityId);
        this.entityId = entityId;
        this.status = status;
    }
}
