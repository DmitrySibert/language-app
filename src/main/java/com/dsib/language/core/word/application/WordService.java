package com.dsib.language.core.word.application;

import com.dsib.language.core.word.domain.Word;
import com.dsib.language.core.word.domain.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<String> getAllTags() {
        return wordRepository.getAllTags();
    }

    public List<Word> getByTags(List<String> tags) {
        return wordRepository.getByTags(tags);
    }

    public List<Word> getByOrigin(List<String> origins) {
        return wordRepository.getByOrigin(origins);
    }

    public List<Word> getRandom(int size) {
        return wordRepository.getRandom(size);
    }
}
