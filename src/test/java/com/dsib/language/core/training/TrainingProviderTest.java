package com.dsib.language.core.training;

import com.dsib.language.core.training.application.TrainingProvider;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingRepository;
import com.dsib.language.core.training.domain.TrainingService;
import com.dsib.language.core.training.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingProviderTest {

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingRepository trainingRepository;

    private TrainingProvider trainingProvider;

    @BeforeEach
    public void setUp() {
        trainingProvider = new TrainingProvider(trainingService, trainingRepository);
    }

    @Test
    public void testGetTrainingByTags() {
        Training training = mock(Training.class);
        when(trainingService.getSimpleTrainingByTags(eq(List.of("tag1"))))
            .then(args -> training);
        when(trainingRepository.create(eq(training)))
            .then(args -> args.getArgument(0));

        Training actualTraining = trainingProvider.getTraining(TrainingType.TAGGED, List.of("tag1"), null);

        assertNotNull(actualTraining);
    }

    @Test
    public void testGetSimpleRepeatTraining() {
        Training training = mock(Training.class);
        when(trainingService.getSimpleRepeatTraining(eq(2)))
            .then(args -> training);
        when(trainingRepository.create(eq(training)))
            .then(args -> args.getArgument(0));

        Training actualTraining = trainingProvider.getTraining(TrainingType.REPEAT, null, 2);

        assertNotNull(actualTraining);
    }

    @Test
    public void testGetSimpleRandomTraining() {
        Training training = mock(Training.class);
        when(trainingService.getSimpleRandomTraining(eq(2)))
            .then(args -> training);
        when(trainingRepository.create(eq(training)))
            .then(args -> args.getArgument(0));

        Training actualTraining = trainingProvider.getTraining(TrainingType.RANDOM, null, 2);

        assertNotNull(actualTraining);
    }
}
