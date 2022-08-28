package com.dsib.language.core.word.domain;

import java.util.List;

public interface WordRepository {
  List<Word> getByTags(List<String> tags);
  List<Word> getByOrigin(List<String> origins);
  List<Word> getRandom(int size);
  void upsert(List<Word> words);
}
