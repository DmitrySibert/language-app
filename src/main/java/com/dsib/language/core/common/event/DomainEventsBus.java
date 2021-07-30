package com.dsib.language.core.common.event;

import java.util.function.Consumer;

public interface DomainEventsBus {
    void publish(DomainEvent domainEvent);

    void subscribe(String id, String eventName, Consumer<DomainEvent> domainEventConsumer);

    void unsubscribe(String id, String eventName);

    void unsubscribeAll(String id);
}
