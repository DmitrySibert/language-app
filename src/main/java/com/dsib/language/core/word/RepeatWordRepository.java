package com.dsib.language.core.word;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatWordRepository extends CrudRepository<RepeatWordEntity, String> {
}