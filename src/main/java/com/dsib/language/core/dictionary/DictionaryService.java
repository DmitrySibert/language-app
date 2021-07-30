package com.dsib.language.core.dictionary;

import com.dsib.language.core.word.WordService;
import com.dsib.language.core.word.Word;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
public class DictionaryService {

    private final WordService wordService;

    public DictionaryService(WordService wordService) {
        this.wordService = wordService;
    }

    private List<Word> readAllWords(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader);
        List<Word> words = new LinkedList<>();
        csvReader.forEach(
            wordData -> words.add(parseWordData(wordData))
        );
        reader.close();
        csvReader.close();

        return words;
    }

    private Word parseWordData(String[] wordData) {
        return new Word(
            wordData[0], wordData[1],
            Arrays.asList(wordData[2].strip().split(";")),
            Arrays.asList(wordData[3].strip().split(";"))
        );
    }

    public void updateDictionary(InputStream csv) {
        try (Reader csvReader = new InputStreamReader(csv)) {
            wordService.upsert(readAllWords(csvReader));
        } catch (Exception e) {
            throw new RuntimeException("Dictionary service: update dictionary from file error:", e);
        }
    }

    public void updateDictionary(List<InputStream> csvs) {
        csvs.forEach(this::updateDictionary);
    }
}
