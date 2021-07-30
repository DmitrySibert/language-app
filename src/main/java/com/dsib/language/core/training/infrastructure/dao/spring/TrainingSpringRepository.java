package com.dsib.language.core.training.infrastructure.dao.spring;

import com.dsib.language.core.training.infrastructure.dao.TrainingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSpringRepository extends CrudRepository<TrainingEntity, String> {
}
