package com.dsib.language.core.training.application.file;

import com.dsib.language.core.training.application.TrainingProvider;
import com.dsib.language.core.training.domain.TrainingType;
import com.dsib.language.core.training.domain.Training;
import com.dsib.language.core.training.application.file.csv.CsvRow;
import com.dsib.language.core.training.application.file.csv.TrainingCsvRow;
import com.dsib.language.core.word.application.WordService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileTrainingProvider {

  private static final String TRAINING_FILE_NAME = "training";
  private static final String TRAINING_FILE_EXTENSION_CSV = "csv";

  private final TrainingProvider trainingProvider;
  private final WordService wordService;

  public FileTrainingProvider(TrainingProvider trainingProvider, WordService wordService) {
    this.trainingProvider = trainingProvider;
    this.wordService = wordService;
  }

  public FileTraining build(List<String> tags)
    throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException
  {
    Training training = trainingProvider.getTraining(TrainingType.TAGGED, tags, null);
    try (
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(baos)
    ) {
      List<CsvRow> trainingWords = new LinkedList<>();
      trainingWords.add(TrainingCsvRow.TITLE);
      wordService.getByOrigin(training.getTrainingSet().getWords()).forEach(word -> trainingWords.add(
        new TrainingCsvRow(word.getWordTranslate(), "", String.join(",", word.getWordInfo()))
      ));

      StatefulBeanToCsv<CsvRow> csvRowsWriter = new StatefulBeanToCsvBuilder<CsvRow>(writer)
        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
        .build();
      csvRowsWriter.write(trainingWords);
      writer.flush();

      return new FileTraining(
        new ByteArrayInputStream(baos.toByteArray()), baos.size(),
        TRAINING_FILE_NAME, TRAINING_FILE_EXTENSION_CSV
      );
    }
  }
}
