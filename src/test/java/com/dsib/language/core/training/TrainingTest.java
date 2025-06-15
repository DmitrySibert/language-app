package com.dsib.language.core.training;

import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingDomainEvent;
import com.dsib.language.core.training.domain.TrainingStatus;
import com.dsib.language.core.training.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingTest {

  private Training training;

  @BeforeEach
  public void setUp() {
    training = new Training("id", TrainingStatus.CREATED, TrainingType.RANDOM, 10, List.of(), LocalDateTime.MIN, List.of());
  }

  @Test
  public void testCantOverwriteId() {
    assertThrows(IllegalStateException.class, () -> training.setId("newId"));
  }

  @Test
  public void testCompletedNotChanged() {
    training.setStatus(TrainingStatus.COMPLETED);

    assertThrows(IllegalStateException.class, () -> training.setStatus(TrainingStatus.COMPLETED));
  }

  @Test
  public void testCantCompleteCompleted() {
    training.setStatus(TrainingStatus.COMPLETED);

    assertThrows(IllegalStateException.class, () -> training.complete(List.of(), List.of()));
  }

  @Test
  public void testCantSetCompletedAtNull() {
    assertThrows(IllegalStateException.class, () -> training.setCompletedAt(null));
  }

  @Test
  public void testComplete() {
    training.complete(List.of(), List.of());

    assertEquals(TrainingStatus.COMPLETED, training.getStatus());
    assertNotNull(training.getCompletedAt());
  }

  @Test
  public void testComplete_DomainEventProduced() {
    TrainingDomainEvent trainingDomainEvent = training.complete(List.of(), List.of());

    assertEquals(trainingDomainEvent.getEntityId(), training.getId());
    assertEquals(trainingDomainEvent.getStatus(), training.getStatus());
  }

  @Test
  public void testComplete_OutcomeUpdated() {
    List<String> words = new ArrayList<>();
    words.add("word1");
    words.add("word2");
    words.add("word3");
    training.getSet().setWords(words);

    training.complete(List.of("word1"), List.of("word2"));

    assertEquals("word3", training.getSet().getWords().get(0));
    assertEquals("word1", training.getSet().getApproved().get(0));
    assertEquals("word2", training.getSet().getFailed().get(0));
    assertEquals(1, training.getSet().getWords().size());
    assertEquals(1, training.getSet().getApproved().size());
    assertEquals(1, training.getSet().getFailed().size());
  }

  private static class Job {

    private Integer num;

    public Job(Integer num) {
      this.num = num;
    }

    private String runJob() {
      System.out.println(Thread.currentThread().getName() + " res: " + num);
      return num.toString();
    };

  }


  @Test
  public void jobRunner() {

    Map<Integer, List<Job>> jobs = new HashMap<>(){{
      put(1, List.of(new Job(1), new Job(2), new Job(3)));
      put(2, List.of(new Job(11), new Job(12), new Job(13)));
      put(3, List.of(new Job(21), new Job(22), new Job(23)));
    }};

    CompletableFuture<Void> proxyFuture = CompletableFuture.completedFuture(null);

    for (var group : jobs.entrySet()) {
      proxyFuture = proxyFuture.thenCompose(prevRes -> {

        List<CompletableFuture<String>> groupFutures = group.getValue().stream()
                .map(job -> CompletableFuture.supplyAsync(job::runJob))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(groupFutures.toArray(new CompletableFuture[]{}));
      });
    }
  }
}
