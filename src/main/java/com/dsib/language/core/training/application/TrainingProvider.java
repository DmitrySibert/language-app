package com.dsib.language.core.training.application;

import com.dsib.language.core.training.Training;
import com.dsib.language.core.training.TrainingRepository;
import com.dsib.language.core.training.TrainingService;
import com.dsib.language.core.training.TrainingType;

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
