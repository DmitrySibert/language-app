package com.dsib.language.core.common.event;

import lombok.Getter;

public class DomainEvent {

    @Getter
    private final String name;

    public DomainEvent(String name) {
        this.name = name;
    }
}
