package com.dsib.language.core.training;


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

    private TrainingProvider trainingProvider;
    private TrainingFinisher trainingFinisher;

    public TrainingController(TrainingProvider trainingProvider, TrainingFinisher trainingFinisher) {
        this.trainingProvider = trainingProvider;
        this.trainingFinisher = trainingFinisher;
    }

//    @CrossOrigin(origins = "http://localhost")
    @GetMapping
    public Training getTraining(@RequestParam List<String> tags, @RequestParam String type) {
        Training training = trainingProvider.getTraining(TrainingType.valueOf(type), tags);
        return training;
    }

//    @CrossOrigin(origins = "http://localhost")
    @PostMapping
    public void getTraining(@RequestBody Training training) {
        trainingFinisher.finishTrainingSession(training);
    }
}
