package com.dsib.language.core.training.presentation.file.csv;

import com.dsib.language.core.training.presentation.file.FileTraining;
import com.dsib.language.core.training.domain.Training;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

public class TrainingToCsvFileBuilder {

    private static final String TRAINING_FILE_EXTENSION_CSV = "csv";

    public FileTraining build(String filename, Training training)
      throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos)
        ) {
            List<CsvRow> trainingWords = new LinkedList<>();
            trainingWords.add(TrainingCsvRow.TITLE);
            training.getTrainingSession().getWords().forEach(word -> trainingWords.add(
                    new TrainingCsvRow(word.getWordTranslate(), "", String.join(",", word.getWordInfo()))
            ));

            StatefulBeanToCsv<CsvRow> csvRowsWriter = new StatefulBeanToCsvBuilder<CsvRow>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            csvRowsWriter.write(trainingWords);
            writer.flush();

            return new FileTraining(
                    new ByteArrayInputStream(baos.toByteArray()), baos.size(),
                    filename, TRAINING_FILE_EXTENSION_CSV
            );
        }
    }
}
