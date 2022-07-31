package com.dsib.language.core.word.domain;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;
import com.dsib.language.core.word.infrastructure.dao.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getByTags(List<String> tags) {
        return WordEntityMapper.fromEntity(wordRepository.findByTags(tags));
    }

    public List<Word> getByOrigin(List<String> origins) {
        return WordEntityMapper.fromEntity(wordRepository.findAllById(origins));
    }

    public List<Word> getAll() {
        return WordEntityMapper.fromEntity(wordRepository.findAll());
    }

    public void upsert(List<Word> words) {
        List<WordEntity> wordEntities = words.stream()
            .map(WordEntityMapper::toNewEntity)
            .collect(Collectors.toList());
        wordRepository.upsert(wordEntities);
    }
}
