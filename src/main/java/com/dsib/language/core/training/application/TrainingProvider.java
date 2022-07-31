package com.dsib.language.core.training.application;

import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingRepository;
import com.dsib.language.core.training.domain.TrainingService;
import com.dsib.language.core.training.domain.TrainingType;

import java.util.List;

public class TrainingProvider {

    private final TrainingService trainingService;
    private final TrainingRepository trainingRepository;

    public TrainingProvider(
            TrainingService trainingService, TrainingRepository trainingRepository
    ) {
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
    }

    public Training getTraining(TrainingType type, List<String> tags, Integer size) {
        Training training;
        switch (type) {
            case RANDOM: {
                training = trainingService.getSimpleRandomTraining(size);
                break;
            }
            case REPEAT: {
                training = trainingService.getSimpleRepeatTraining(size);
                break;
            }
            case TAGGED: {
                training = trainingService.getSimpleTrainingByTags(tags);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return trainingRepository.create(training);
    }
}
