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
  public List<String> getAllTags(String ownerId) {
    return wordSpringRepository.findAllTagsByOwnerId(ownerId);
  }

  @Override
  public List<Word> getByTags(List<String> tags, Integer size, String ownerId) {
    if (size == null) {
      return WordEntityMapper.fromEntity(wordSpringRepository.findByTags(tags, ownerId));
    } else {
      //TODO: SELECT * FROM big TABLESAMPLE SYSTEM_ROWS(1000);
      //TODO: generate random indexes in code, select with artificial sequence id and filter by this artificial id in generated indexes
      List<WordEntity> wordEntities = wordSpringRepository.findByTags(tags, ownerId);
      int actualSize = Math.min(wordEntities.size(), size);
      Collections.shuffle(wordEntities);
      return WordEntityMapper.fromEntity(wordEntities.subList(0, actualSize));
    }
  }

  @Override
  public List<Word> getByOrigin(List<String> origins, String ownerId) {
    return WordEntityMapper.fromEntity(wordSpringRepository.findByOriginInAndOwnerId(origins, ownerId));
  }

  @Override
  public List<Word> getRandom(int size, String ownerId) {
    //TODO: SELECT * FROM big TABLESAMPLE SYSTEM_ROWS(1000);
    //TODO: generate random indexes in code, select with artificial sequence id and filter by this artificial id in generated indexes
    List<WordEntity> wordEntities = wordSpringRepository.findByOwnerId(ownerId);
    int actualSize = Math.min(wordEntities.size(), size);
    Collections.shuffle(wordEntities);
    return WordEntityMapper.fromEntity(wordEntities.subList(0, actualSize));
  }

  @Override
  public void upsert(List<Word> words, String ownerId) {
    List<WordEntity> wordEntities = words.stream()
      .map(word -> WordEntityMapper.toNewEntity(word, ownerId))
      .collect(Collectors.toList());
    wordSpringRepository.upsert(wordEntities);
  }
}
