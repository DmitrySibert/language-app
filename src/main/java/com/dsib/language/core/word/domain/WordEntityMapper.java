package com.dsib.language.core.word.domain;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;

import java.util.LinkedList;
import java.util.List;

public class WordEntityMapper {

    public static Word fromEntity(WordEntity wordEntity) {
        return new Word(
            wordEntity.getOrigin(),
            wordEntity.getData().getTranslate(),
            wordEntity.getData().getWordInfo(),
            wordEntity.getData().getTags()
        );
    }

    public static List<Word> fromEntity(Iterable<WordEntity> wordEntities) {

        List<Word> words = new LinkedList<>();
        wordEntities.forEach(wordEntity -> words.add(fromEntity(wordEntity)));

        return words;
    }

    public static WordEntity toEntity(Word word) {

        return new WordEntity(
                word.getWordOrigin(),
                word.getWordTranslate(),
                word.getWordInfo(),
                word.getTags(),
                false
        );
    }

    public static WordEntity toNewEntity(Word word) {

        return new WordEntity(
                word.getWordOrigin(),
                word.getWordTranslate(),
                word.getWordInfo(),
                word.getTags(),
                true
        );
    }
}
