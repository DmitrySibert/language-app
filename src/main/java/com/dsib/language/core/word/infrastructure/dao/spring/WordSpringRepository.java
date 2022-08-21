package com.dsib.language.core.word.infrastructure.dao.spring;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSpringRepository extends CrudRepository<WordEntity, String>, WordSpringRepositoryExtension {
}
