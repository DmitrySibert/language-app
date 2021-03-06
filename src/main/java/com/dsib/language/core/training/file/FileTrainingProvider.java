package com.dsib.language.core.training.file;

import com.dsib.language.core.training.Training;
import com.dsib.language.core.training.TrainingProvider;
import com.dsib.language.core.training.file.csv.CsvRow;
import com.dsib.language.core.training.file.csv.TrainingCsvRow;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileTrainingProvider {

    private static final String TRAINING_FILE_NAME = "training";
    private static final String TRAINING_FILE_EXTENSION_CSV = "csv";

    private final TrainingProvider trainingProvider;

    public FileTrainingProvider(TrainingProvider trainingProvider) {
        this.trainingProvider = trainingProvider;
    }

    public FileTraining getSimpleTrainingByTags(List<String> tags) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        Training training = trainingProvider.getSimpleTrainingByTags(tags);
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(baos)
        ) {
            List<CsvRow> trainingWords = new LinkedList<>();
            trainingWords.add(TrainingCsvRow.TITLE);
            training.getWords().forEach(word -> trainingWords.add(
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
