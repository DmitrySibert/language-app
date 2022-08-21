package com.dsib.language.core.word.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getByTags(List<String> tags) {
        return wordRepository.getByTags(tags);
    }

    public List<Word> getByOrigin(List<String> origins) {
        return wordRepository.getByOrigin(origins);
    }

    public List<Word> getAll() {
        return wordRepository.getAll();
    }

    public void upsert(List<Word> words) {
        wordRepository.upsert(words);
    }
}
