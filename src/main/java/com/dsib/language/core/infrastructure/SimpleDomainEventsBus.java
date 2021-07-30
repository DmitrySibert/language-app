package com.dsib.language.core.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class SimpleDomainEventsBus implements DomainEventsBus {

    private Map<String, Consumer<DomainEvent>> eventConsumers;
    //<EventName, ID>
    private Map<Pair<String, String>, Consumer<DomainEvent>> subscriptions;

    public SimpleDomainEventsBus() {
        eventConsumers = new HashMap<>();
        subscriptions = new HashMap<>();
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Event {} is published", domainEvent.getName());
        eventConsumers.get(domainEvent.getName())
            .accept(domainEvent);
    }

    @Override
    public void subscribe(String id, String eventName, Consumer<DomainEvent> domainEventConsumer) {
        eventConsumers.put(eventName, domainEventConsumer);
        subscriptions.put(Pair.of(eventName, id), domainEventConsumer);
    }

    @Override
    public void unsubscribe(String id, String eventName) {
        eventConsumers.remove(eventName);
        subscriptions.remove(Pair.of(eventName, id));
    }

    @Override
    public void unsubscribeAll(String id) {
        subscriptions.keySet().stream()
            .filter(subscription -> subscription.getSecond().equals(id))
            .forEach(subscription -> {
                eventConsumers.remove(subscription.getFirst());
                subscriptions.remove(Pair.of(subscription.getFirst(), id));
            });
    }
}
