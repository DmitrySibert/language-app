package com.dsib.language.core.word.infrastructure.dao.spring;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WordSpringRepository extends CrudRepository<WordEntity, String>, WordSpringRepositoryExtension {

  @Query(
    "SELECT DISTINCT jsonb_array_elements_text(we.\"data\"->'tags') FROM word_entity we WHERE we.owner_id = :ownerId"
  )
  List<String> findAllTagsByOwnerId(@Param("ownerId") String ownerId);
  List<WordEntity> findByOwnerId(String ownerId);
  List<WordEntity> findByOriginInAndOwnerId(Collection<String> origins, String ownerId);
}
