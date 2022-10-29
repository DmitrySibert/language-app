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

    public List<String> getAllTags(String ownerId) {
        return wordRepository.getAllTags(ownerId);
    }

    public List<Word> getByTags(List<String> tags, String ownerId) {
        return wordRepository.getByTags(tags, ownerId);
    }

    public List<Word> getByOrigin(List<String> origins, String ownerId) {
        return wordRepository.getByOrigin(origins, ownerId);
    }

    public List<Word> getRandom(int size, String ownerId) {
        return wordRepository.getRandom(size, ownerId);
    }
}
