package com.dsib.language.core.progress.application;

import com.dsib.language.core.common.event.DomainEvent;
import com.dsib.language.core.common.event.DomainEventsBus;
import com.dsib.language.core.progress.domain.WordProgress;
import com.dsib.language.core.progress.domain.WordProgressRepository;
import com.dsib.language.core.training.domain.TrainingDomainEvent;
import com.dsib.language.core.training.domain.TrainingSession;
import com.dsib.language.core.word.domain.Word;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WordProgressUpdater {

    private final static String ID = UUID.randomUUID().toString();
    private final static String TRANSACTION_TEMPLATE_NAME = "WordProgressServiceTT";
    private final static String TRAINING_DOMAIN_EVENT_NAME = "TrainingDomainEvent";
    private final WordProgressRepository wordProgressRepository;
    private final DomainEventsBus domainEventsBus;
    private final TransactionTemplate transactionTemplate;

    public WordProgressUpdater(
        WordProgressRepository wordProgressRepository, DomainEventsBus domainEventsBus,
        TransactionTemplate transactionTemplate
    ) {
        this.wordProgressRepository = wordProgressRepository;
        this.domainEventsBus = domainEventsBus;
        this.domainEventsBus.subscribe(ID, TRAINING_DOMAIN_EVENT_NAME, this::handleTrainingDomainEvent);
        this.transactionTemplate = transactionTemplate;
        this.transactionTemplate.setName(TRANSACTION_TEMPLATE_NAME);
    }

    public Iterable<WordProgress> update(Iterable<WordProgress> wordsProgress) {
        return wordProgressRepository.update(wordsProgress);
    }

    //TODO: check if this code should be decomposed to application and domain logic
    private void handleTrainingDomainEvent(DomainEvent domainEvent) {
        TrainingDomainEvent trainingDomainEvent = (TrainingDomainEvent) domainEvent;
        TrainingSession trainingSession = trainingDomainEvent.getTraining().getTrainingSession();
        List<String> wordsForRepetition = trainingSession.getWordsForRepetition().stream()
                .map(Word::getWordOrigin)
                .collect(Collectors.toList());
        List<String> approvedWords = trainingSession.getApprovedWords().stream()
                .map(Word::getWordOrigin)
                .collect(Collectors.toList());
        //performance: reduce number of DB calls
        List<String> allTrainedWords = new ArrayList<>(wordsForRepetition.size() + approvedWords.size());
        allTrainedWords.addAll(wordsForRepetition);
        allTrainedWords.addAll(approvedWords);

        transactionTemplate.execute(status -> {
            Map<String, WordProgress> wordsProgressByOrigin = wordProgressRepository.findAllByOrigin(allTrainedWords)
                    .parallelStream()
                    .collect(Collectors.toMap(WordProgress::getOrigin, Function.identity()));

            List<WordProgress> newWordsProgress = new ArrayList<>(allTrainedWords.size());
            List<WordProgress> updatedWordsProgress = new ArrayList<>(allTrainedWords.size());
            allTrainedWords.forEach(trainedWordOrigin -> {
                WordProgress existing = wordsProgressByOrigin.get(trainedWordOrigin);
                if (existing == null) {
                    WordProgress wordProgress = new WordProgress(trainedWordOrigin);
                    wordsProgressByOrigin.put(trainedWordOrigin, wordProgress);
                    newWordsProgress.add(wordProgress);
                } else {
                    updatedWordsProgress.add(wordsProgressByOrigin.get(trainedWordOrigin));
                }

            });

            wordsForRepetition.forEach(failedWordOrigin -> wordsProgressByOrigin.get(failedWordOrigin).addFail());
            approvedWords.forEach(approvedWordOrigin -> wordsProgressByOrigin.get(approvedWordOrigin).addApprove());

            wordProgressRepository.update(updatedWordsProgress);
            wordProgressRepository.create(newWordsProgress);

            return wordsProgressByOrigin.values();
        });
    }
}
