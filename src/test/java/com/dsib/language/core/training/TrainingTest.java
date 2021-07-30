package com.dsib.language.core.training;

import com.dsib.language.core.word.Word;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingTest {

    private final Word word1 = new Word(
            "word1", "translate1", new String[]{"info1", "info2"}, new String[]{"tag1", "tag2"}
    );
    private final Word word2 = new Word(
            "word2", "translate2", new String[]{"info1", "info2"}, new String[]{"tag1", "tag2"}
    );

    @Test
    public void testStartTraining_NewTraining() {
        Training training = new Training(List.of(word1, word2));
        training.start();

        boolean move1Res = training.moveNext();
        boolean move2Res = training.moveNext();

        assertTrue(move1Res);
        assertFalse(move2Res);
        assertTrue(training.getApprovedWords().isEmpty());
        assertTrue(training.getWordsForRepetition().isEmpty());
    }

    @Test
    public void testStartTraining_UsedTraining() {
        Training training = new Training(List.of(word1, word2));

        training.moveNext();
        training.approveCurrentWord();
        training.addCurrentWordToRepeat();
        training.start();

        boolean move1Res = training.moveNext();
        boolean move2Res = training.moveNext();

        assertTrue(move1Res);
        assertFalse(move2Res);
        assertTrue(training.getApprovedWords().isEmpty());
        assertTrue(training.getWordsForRepetition().isEmpty());
    }

    @Test
    public void testMoveNext() {
        Training training = new Training(List.of(word1, word2));

        Word word1 = training.getCurrent();
        training.moveNext();
        Word word2 = training.getCurrent();

        assertEquals(word1.getWordOrigin(), this.word1.getWordOrigin());
        assertEquals(word2.getWordOrigin(), this.word2.getWordOrigin());
    }

    @Test
    public void testIsEnd() {
        Training training = new Training(List.of(word1, word2));

        training.moveNext();

        assertTrue(training.isEnd());
    }

    @Test
    public void testAddToRepeat() {
        Training training = new Training(List.of(word1));

        training.addCurrentWordToRepeat();

        assertEquals(1, training.getWordsForRepetition().size());
        assertEquals(training.getWordsForRepetition().get(0).getWordOrigin(), word1.getWordOrigin());
    }

    @Test
    public void testApproveWord() {
        Training training = new Training(List.of(word1));

        training.approveCurrentWord();

        assertEquals(1, training.getApprovedWords().size());
        assertEquals(training.getApprovedWords().get(0).getWordOrigin(), word1.getWordOrigin());
    }
}
