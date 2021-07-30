package com.dsib.language.core.dictionary;

import com.dsib.language.core.word.WordEntity;
import com.dsib.language.core.word.WordMapper;
import com.dsib.language.core.word.WordRepository;
import com.dsib.language.core.word.Word;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DictionaryService {

    private List<Word> dictionary;
    private Map<String, List<Word>> searchIndexByTag;
    private WordRepository wordRepository;

    public DictionaryService(WordRepository wordRepository) {
        dictionary = new ArrayList<>();
        this.wordRepository = wordRepository;
    }

    @PostConstruct
    public void init() {
        wordRepository.findAll().forEach(word -> dictionary.add(WordMapper.fromWordEntity(word)));
        buildSearchIndexByTag();
    }

    private void buildSearchIndexByTag() {
        searchIndexByTag = new HashMap<>();
        dictionary.forEach(wordData ->
            wordData.getTags().forEach(tag -> {
                var taggedWords = searchIndexByTag.get(tag);
                if (taggedWords == null) {
                    searchIndexByTag.put(tag, new ArrayList<>());
                    taggedWords = searchIndexByTag.get(tag);
                }
                taggedWords.add(wordData);
            })
        );
    }

    public List<Word> getWordsByTags(List<String> tags) {
        List<Word> words = new ArrayList<>();
        tags.forEach(tag ->
            words.addAll(searchIndexByTag.getOrDefault(tag, List.of()))
        );
        return words;
    }

    public List<Word> getWordsByOrigin(List<String> origins) {
        Iterable<WordEntity> wordEntities = wordRepository.findAllById(origins);
        List<Word> words = new ArrayList<>(origins.size());
        for (WordEntity wordEntity: wordEntities) {
            words.add(WordMapper.fromWordEntity(wordEntity));
        }
        return words;
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
        return new Word(wordData[0], wordData[1], wordData[2].strip().split(";"), wordData[3].strip().split(";"));
    }

    public void updateDictionary(InputStream csv) {
        try (Reader csvReader = new InputStreamReader(csv)) {
            List<WordEntity> words = readAllWords(csvReader)
                    .stream()
                    .map(WordMapper::fromWord)
                    .collect(Collectors.toList());
            wordRepository.upsert(words);
        } catch (Exception e) {
            throw new RuntimeException("Dictionary service: update dictionary from file error:", e);
        }
    }

    public void updateDictionary(List<InputStream> csvs) {
        csvs.forEach(this::updateDictionary);
    }
}
