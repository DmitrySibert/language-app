package com.dsib.language.core.progress.domain;

import java.util.List;

public interface WordProgressRepository {
    List<WordProgress> findAll();

    List<WordProgress> findAllByOrigin(List<String> origins);

    Iterable<WordProgress> update(Iterable<WordProgress> wordsProgress);

    Iterable<WordProgress> create(Iterable<WordProgress> wordsProgress);

    Iterable<WordProgress> findMostFailedNonApproved(int number);

    Iterable<WordProgress> findOldNonApproved(int number);
}
