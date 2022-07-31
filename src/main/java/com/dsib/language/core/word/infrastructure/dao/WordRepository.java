package com.dsib.language.core.word.infrastructure.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends CrudRepository<WordEntity, String>, WordRepositoryExtension {
}
