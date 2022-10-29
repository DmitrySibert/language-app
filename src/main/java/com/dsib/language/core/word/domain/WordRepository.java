package com.dsib.language.core.word.domain;

import java.util.List;

public interface WordRepository {

  List<String> getAllTags(String ownerId);
  List<Word> getByTags(List<String> tags, String ownerId);
  List<Word> getByOrigin(List<String> origins, String ownerId);
  List<Word> getRandom(int size, String ownerId);
  void upsert(List<Word> words, String ownerId);
}
