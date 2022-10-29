package com.dsib.language.core.word.application.dictionary;

import com.dsib.language.core.word.domain.Word;
import com.dsib.language.core.word.domain.WordRepository;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class parse dictionary files and requests to save them
 */
@Service
public class DictionaryService {

    private final WordRepository wordRepository;

    public DictionaryService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
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

    public void updateDictionary(InputStream csv, String ownerId) {
        try (Reader csvReader = new InputStreamReader(csv)) {
            wordRepository.upsert(readAllWords(csvReader), ownerId);
        } catch (Exception e) {
            throw new RuntimeException("Dictionary service: update dictionary from file error:", e);
        }
    }

    public void updateDictionary(List<InputStream> csvs, String ownerId) {
        csvs.forEach(csv -> this.updateDictionary(csv, ownerId));
    }
}
