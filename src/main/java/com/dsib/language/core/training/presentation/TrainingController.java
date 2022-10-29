package com.dsib.language.core.training.presentation;

import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.application.TrainingFinisher;
import com.dsib.language.core.training.domain.TrainingType;
import com.dsib.language.core.training.application.TrainingProvider;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dsib.language.core.word.presentation.Constants.X_USER_ID_HEADER;

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
//TODO: should return Training DTO instead of a domain model
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
    @RequestParam(required = false) Integer size,
    @RequestHeader(value = X_USER_ID_HEADER) String userId
  ) {
    return trainingProvider.createNewTraining(type, tags, size, userId);
  }

  @PostMapping
  public Training finish(@RequestBody FinishTrainingDto training) {
    return trainingFinisher.finishTrainingSession(training);
  }
}
