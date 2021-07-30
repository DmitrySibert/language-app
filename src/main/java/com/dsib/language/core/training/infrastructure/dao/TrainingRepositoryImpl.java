package com.dsib.language.core.training.infrastructure.dao;

import com.dsib.language.core.training.Training;
import com.dsib.language.core.training.TrainingRepository;
import com.dsib.language.core.training.infrastructure.dao.spring.TrainingSpringRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    private TrainingSpringRepository springRepository;
    private final TrainingEntityMapper entityMapper;

    public TrainingRepositoryImpl(TrainingSpringRepository springRepository) {
        this.springRepository = springRepository;
        entityMapper = new TrainingEntityMapper();
    }

    @Override
    public Training create(Training training) {
        TrainingEntity trainingEntity = springRepository.save(entityMapper.toNewEntity(training));
        //TODO: get rid of manually created IDs
//        training.setId(trainingEntity.getId());
        return training;
    }

    @Override
    public Optional<Training> find(String id) {
        return springRepository.findById(id).map(entityMapper::fromEntity);
    }

    @Override
    public Training update(Training training) {
        springRepository.save(entityMapper.toEntity(training));
        return training;
    }
}
