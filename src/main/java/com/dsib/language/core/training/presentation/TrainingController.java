package com.dsib.language.core.training.presentation;

import com.dsib.language.core.training.Training;
import com.dsib.language.core.training.application.TrainingFinisher;
import com.dsib.language.core.training.TrainingType;
import com.dsib.language.core.training.application.TrainingProvider;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dictionary rest controller.
 */
@RestController
@RequestMapping(value = "/api/v1/training")
@SwaggerDefinition(
        info = @Info(
                title = "Training",
                description = "Training management endpoint",
                version = "1.0.0"
        )
)
public class TrainingController {

    private final TrainingProvider trainingProvider;
    private final TrainingFinisher trainingFinisher;

    public TrainingController(TrainingProvider trainingProvider, TrainingFinisher trainingFinisher) {
        this.trainingProvider = trainingProvider;
        this.trainingFinisher = trainingFinisher;
    }

    @GetMapping
    public Training get(
            @RequestParam(required = false) List<String> tags,
            @RequestParam TrainingType type,
            @RequestParam(required = false) Integer size
    ) {
        Training training = trainingProvider.getTraining(type, tags, size);
        return training;
    }

    @PostMapping
    public void finish(@RequestBody FinishTrainingDto training) {
        trainingFinisher.finishTrainingSession(training);
    }
}
