package com.dsib.language.core.progress.domain;

import java.util.List;

public interface WordProgressRepository {
    List<WordProgress> findAll(String ownerId);

    List<WordProgress> findAllByOrigin(List<String> origins, String ownerId);

    Iterable<WordProgress> update(Iterable<WordProgress> wordsProgress);

    Iterable<WordProgress> create(Iterable<WordProgress> wordsProgress);

    Iterable<WordProgress> findMostFailedNonApproved(int number, String ownerId);

    Iterable<WordProgress> findOldNonApproved(int number, String ownerId);
}
