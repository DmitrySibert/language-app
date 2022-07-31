package com.dsib.language.core.training;

import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingStatus;
import com.dsib.language.core.training.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrainingTest {

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training("id", TrainingStatus.CREATED, TrainingType.RANDOM, 10, List.of(), LocalDateTime.MIN);
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

        assertThrows(IllegalStateException.class, () -> training.complete());
    }

    @Test
    public void testCantSetCompletedAtNull() {
        assertThrows(IllegalStateException.class, () -> training.setCompletedAt(null));
    }
}
