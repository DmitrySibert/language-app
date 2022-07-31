//package com.dsib.language.core.training;
//
//import com.dsib.language.core.progress.domain.ProgressType;
//import com.dsib.language.core.progress.domain.WordProgress;
//import com.dsib.language.core.progress.domain.WordProgressFailProneService;
//import com.dsib.language.core.word.domain.Word;
//import com.dsib.language.core.word.domain.WordService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class InteractiveTrainingProviderTest {
//
//    @Mock
//    private WordService wordService;
//    @Mock
//    private WordProgressFailProneService wordProgressFailProneService;
//
//    private final Word word1 = new Word(
//            "word1", "translate1", List.of("info1", "info2"), List.of("tag1", "tag2")
//    );
//    private final Word word2 = new Word(
//            "word2", "translate2", List.of("info1", "info2"), List.of("tag1", "tag2")
//    );
//    private final WordProgress wordProgress1 = new WordProgress("word1");
//    private final WordProgress wordProgress2 = new WordProgress("word2");
//
//    private TrainingProvider trainingProvider;
//
//    @BeforeEach
//    public void setUp() {
//        trainingProvider = new TrainingProvider(wordService, wordProgressFailProneService);
//    }
//
//    @Test
//    public void testGetTrainingByTags() {
//        when(wordService.getByTags(any(List.class)))
//            .then(args -> {
//                List<Word> words = new LinkedList<>();
//                words.add(word1);
//                words.add(word2);
//                return words;
//            });
//
//        InteractiveTraining training = trainingProvider.getSimpleTrainingByTags(List.of());
//
//        List<Word> trainingWords = new LinkedList<>();
//        trainingWords.add(training.getCurrent());
//        training.moveNext();
//        trainingWords.add(training.getCurrent());
//
//        assertEquals(2, trainingWords.size());
//        assertTrue(List.of(word1, word2).containsAll(trainingWords));
//    }
//
//    @Test
//    public void testGetSimpleRepeatTraining() {
//        when(wordProgressFailProneService.getByType(eq(ProgressType.MOST_FAILED), anyInt()))
//            .thenReturn(List.of(wordProgress1, wordProgress2));
//        when(wordService.getByOrigin(List.of(wordProgress1.getOrigin(), wordProgress2.getOrigin())))
//            .then(args -> {
//                List<Word> words = new LinkedList<>();
//                words.add(word1);
//                words.add(word2);
//                return words;
//            });
//
//        InteractiveTraining training = trainingProvider.getSimpleRepeatTraining(10);
//
//        List<Word> trainingWords = new LinkedList<>();
//        trainingWords.add(training.getCurrent());
//        training.moveNext();
//        trainingWords.add(training.getCurrent());
//
//        assertEquals(2, trainingWords.size());
//        assertTrue(List.of(word1, word2).containsAll(trainingWords));
//    }
//
//    @Test
//    public void testGetSimpleRandomTraining() {
//        when(wordService.getAll())
//            .thenReturn(Arrays.asList(word1, word2));
//
//        InteractiveTraining training = trainingProvider.getSimpleRandomTraining(1);
//
//        List<Word> trainingWords = new LinkedList<>();
//        trainingWords.add(training.getCurrent());
//        boolean isEnd = !training.moveNext();
//
//        assertTrue(isEnd);
//        assertTrue(List.of(word1, word2).containsAll(trainingWords));
//    }
//}
