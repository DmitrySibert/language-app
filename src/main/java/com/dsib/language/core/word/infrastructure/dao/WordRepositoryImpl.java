package com.dsib.language.core.word.infrastructure.dao;

import com.dsib.language.core.word.domain.Word;
import com.dsib.language.core.word.domain.WordRepository;
import com.dsib.language.core.word.infrastructure.dao.spring.WordSpringRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WordRepositoryImpl implements WordRepository {

  private final WordSpringRepository wordSpringRepository;

  public WordRepositoryImpl(WordSpringRepository wordSpringRepository) {
    this.wordSpringRepository = wordSpringRepository;
  }

  @Override
  public List<String> getAllTags() {
    return wordSpringRepository.findAllTags();
  }

  @Override
  public List<Word> getByTags(List<String> tags) {
    return WordEntityMapper.fromEntity(wordSpringRepository.findByTags(tags));
  }

  @Override
  public List<Word> getByOrigin(List<String> origins) {
    return WordEntityMapper.fromEntity(wordSpringRepository.findAllById(origins));
  }

  @Override
  public List<Word> getRandom(int size) {
    //TODO: SELECT * FROM big TABLESAMPLE SYSTEM_ROWS(1000);
    List<Word> words = WordEntityMapper.fromEntity(wordSpringRepository.findAll());
    int actualSize = Math.min(words.size(), size);
    Collections.shuffle(words);
    return words.subList(0, actualSize);
  }

  @Override
  public void upsert(List<Word> words) {
    List<WordEntity> wordEntities = words.stream()
      .map(WordEntityMapper::toNewEntity)
      .collect(Collectors.toList());
    wordSpringRepository.upsert(wordEntities);
  }
}
