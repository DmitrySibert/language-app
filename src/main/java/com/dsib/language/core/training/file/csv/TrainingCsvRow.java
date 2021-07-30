package com.dsib.language.core.training.file.csv;

import com.opencsv.bean.CsvBindByPosition;

public class TrainingCsvRow extends CsvRow {

    @CsvBindByPosition(position = 0)
    private String word;

    @CsvBindByPosition(position = 1)
    private String origin;

    @CsvBindByPosition(position = 2)
    private String info;

    public TrainingCsvRow(String word, String origin, String info) {
        this.word = word;
        this.origin = origin;
        this.info = info;
    }

    public static final TrainingCsvRow TITLE = new TrainingCsvRow("WORD", "ORIGIN", "INFO");

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
