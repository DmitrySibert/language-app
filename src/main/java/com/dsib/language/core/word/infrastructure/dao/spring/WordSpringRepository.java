package com.dsib.language.core.word.infrastructure.dao.spring;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordSpringRepository extends CrudRepository<WordEntity, String>, WordSpringRepositoryExtension {

  @Query(
    "SELECT DISTINCT jsonb_array_elements_text(we.\"data\"->'tags') FROM word_entity we"
  )
  List<String> findAllTags();
}
