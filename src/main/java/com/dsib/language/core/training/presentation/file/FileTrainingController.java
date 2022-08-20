package com.dsib.language.core.training.presentation.file;

import com.dsib.language.core.training.application.TrainingProvider;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.domain.TrainingType;
import com.dsib.language.core.training.presentation.file.csv.TrainingToCsvFileBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * File training rest controller.
 */
@RestController
@RequestMapping(value = "/api/v1/training")
@SwaggerDefinition(
  info = @Info(
    title = "File training",
    description = "File training management endpoint",
    version = "1.0.0"
  )
)
public class FileTrainingController {

  private static final String TRAINING_FILE_NAME = "training";

  private final TrainingProvider trainingProvider;
  private final TrainingToCsvFileBuilder trainingToCsvFileBuilder;

  public FileTrainingController(TrainingProvider trainingProvider) {
    this.trainingProvider = trainingProvider;
    trainingToCsvFileBuilder = new TrainingToCsvFileBuilder();
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getTraining(@RequestParam List<String> tags)
    throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

    Training training = trainingProvider.getTraining(TrainingType.TAGGED, tags, null);
    FileTraining fileTraining = trainingToCsvFileBuilder.build(TRAINING_FILE_NAME, training);
    InputStreamResource inputStreamResource = new InputStreamResource(fileTraining.getFile());

    return ResponseEntity.ok()
      .headers(createFileHeaders(fileTraining.getName() + "." + fileTraining.getFileExtension()))
      .contentLength(fileTraining.getFileSize())
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(inputStreamResource);
  }

  private HttpHeaders createFileHeaders(String filename) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    httpHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
    httpHeaders.add(HttpHeaders.PRAGMA, "no-cache");
    httpHeaders.add(HttpHeaders.EXPIRES, "0");

    return httpHeaders;
  }
}
