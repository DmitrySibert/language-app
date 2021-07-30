package com.dsib.language.core.training;

import com.dsib.language.core.infrastructure.DomainEvent;
import lombok.Getter;

import static com.dsib.language.core.util.AssertUtils.assertNotNull;

public class TrainingDomainEvent extends DomainEvent {

    private static final String NAME = "TrainingDomainEvent";

    //TODO: get rid of domain model in Event
    @Getter
    private final Training training;

    public TrainingDomainEvent(Training training) {
        super(NAME);
        assertNotNull("training", training);
        this.training = training;
    }
}
