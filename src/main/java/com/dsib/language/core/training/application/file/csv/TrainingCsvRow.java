package com.dsib.language.core.training.application.file.csv;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

public class TrainingCsvRow extends CsvRow {

    @CsvBindByPosition(position = 0)
    private @Getter @Setter String word;

    @CsvBindByPosition(position = 1)
    private @Getter @Setter  String origin;

    @CsvBindByPosition(position = 2)
    private @Getter @Setter  String info;

    public TrainingCsvRow(String word, String origin, String info) {
        this.word = word;
        this.origin = origin;
        this.info = info;
    }

    public static final TrainingCsvRow TITLE = new TrainingCsvRow("WORD", "ORIGIN", "INFO");
}
