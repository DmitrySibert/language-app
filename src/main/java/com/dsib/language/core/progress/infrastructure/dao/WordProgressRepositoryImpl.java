package com.dsib.language.core.progress.infrastructure.dao;

import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressRepository;
import com.dsib.language.core.progress.infrastructure.dao.spring.WordProgressSpringRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class WordProgressRepositoryImpl implements WordProgressRepository {

    private WordProgressSpringRepository springRepository;
    private final WordProgressEntityMapper entityMapper;

    public WordProgressRepositoryImpl(WordProgressSpringRepository springRepository) {
        this.springRepository = springRepository;
        entityMapper = new WordProgressEntityMapper();
    }

    @Override
    public List<WordProgress> findAll() {
        List<WordProgress> wordsProgress = new LinkedList<>();
        springRepository.findAll()
                .forEach(wordProgressEntityEntity -> wordsProgress.add(entityMapper.fromEntity(wordProgressEntityEntity)));
        return wordsProgress;
    }

    @Override
    public List<WordProgress> findAllByOrigin(List<String> origins) {
        List<WordProgress> wordsProgress = new LinkedList<>();
        springRepository.findAllByOrigin(origins)
                .forEach(wordProgressEntityEntity -> wordsProgress.add(entityMapper.fromEntity(wordProgressEntityEntity)));
        return wordsProgress;
    }

    @Override
    public Iterable<WordProgress> update(Iterable<WordProgress> wordsProgress) {
        List<WordProgressEntity> entities = new LinkedList<>();
        wordsProgress.forEach(wordProgress -> entities.add(entityMapper.toEntityExisting(wordProgress)));
        springRepository.saveAll(entities);
        return wordsProgress;
    }

    @Override
    public Iterable<WordProgress> create(Iterable<WordProgress> wordsProgress) {
        List<WordProgressEntity> entities = new LinkedList<>();
        wordsProgress.forEach(wordProgress -> entities.add(entityMapper.toEntityNew(wordProgress)));
        springRepository.saveAll(entities);
        return wordsProgress;
    }

    @Override
    public Iterable<WordProgress> findMostFailedNonApproved(int number) {
        List<WordProgress> wordsProgress = new LinkedList<>();
        springRepository.findMostFailedNonApproved(number).forEach(entity ->
                wordsProgress.add(entityMapper.fromEntity(entity))
        );
        return wordsProgress;
    }

    @Override
    public Iterable<WordProgress> findOldNonApproved(int number) {
        List<WordProgress> wordsProgress = new LinkedList<>();
        springRepository.findOldNonApproved(number).forEach(entity ->
                wordsProgress.add(entityMapper.fromEntity(entity))
        );
        return wordsProgress;
    }
}
