package com.dsib.language.core.training;

import com.dsib.language.core.dictionary.DictionaryService;
import com.dsib.language.core.word.RepeatWordEntity;
import com.dsib.language.core.word.RepeatWordRepository;
import com.dsib.language.core.word.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingProviderTest {

    @Mock
    private DictionaryService dictionaryService;
    @Mock
    private RepeatWordRepository repeatWordRepository;

    private final Word word1 = new Word(
            "word1", "translate1", new String[]{"info1", "info2"}, new String[]{"tag1", "tag2"}
    );
    private final Word word2 = new Word(
            "word2", "translate2", new String[]{"info1", "info2"}, new String[]{"tag1", "tag2"}
    );
    private final RepeatWordEntity repeatWordEntity1 = new RepeatWordEntity("word1");
    private final RepeatWordEntity repeatWordEntity2 = new RepeatWordEntity("word2");

    private TrainingProvider trainingProvider;

    @BeforeEach
    public void setUp() {
        trainingProvider = new TrainingProvider(dictionaryService, repeatWordRepository);
    }

    @Test
    public void testGetTrainingByTags() {
        when(dictionaryService.getWordsByTags(any(List.class)))
            .then(args -> {
                List<Word> words = new LinkedList<>();
                words.add(word1);
                words.add(word2);
                return words;
            });

        Training training = trainingProvider.getSimpleTrainingByTags(List.of());

        List<Word> trainingWords = new LinkedList<>();
        trainingWords.add(training.getCurrent());
        training.moveNext();
        trainingWords.add(training.getCurrent());

        assertEquals(2, trainingWords.size());
        assertTrue(List.of(word1, word2).containsAll(trainingWords));
    }

    @Test
    public void testGetSimpleRepeatTraining() {
        when(repeatWordRepository.findAll())
            .thenReturn(List.of(repeatWordEntity1, repeatWordEntity2));
        when(dictionaryService.getWordsByOrigin(List.of(repeatWordEntity1.getOrigin(), repeatWordEntity2.getOrigin())))
            .then(args -> {
                List<Word> words = new LinkedList<>();
                words.add(word1);
                words.add(word2);
                return words;
            });

        Training training = trainingProvider.getSimpleRepeatTraining();

        List<Word> trainingWords = new LinkedList<>();
        trainingWords.add(training.getCurrent());
        training.moveNext();
        trainingWords.add(training.getCurrent());

        assertEquals(2, trainingWords.size());
        assertTrue(List.of(word1, word2).containsAll(trainingWords));
    }
}
