package com.dsib.language.core.infrastructure;

import lombok.Getter;

public class DomainEvent {

    @Getter
    private final String name;

    public DomainEvent(String name) {
        this.name = name;
    }
}
