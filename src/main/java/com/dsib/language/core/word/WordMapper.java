package com.dsib.language.core.word;

public class WordMapper {

    public static Word fromWordEntity(WordEntity wordEntity) {
        String[] wordInfo = null;
        if (wordEntity.getWordInfo() != null) {
            wordInfo = wordEntity.getWordInfo().split(";");
        }
        String[] tags = null;
        if (wordEntity.getTags() != null) {
            tags = wordEntity.getTags().split(";");
        }

        return new Word(wordEntity.getOrigin(), wordEntity.getTranslate(), wordInfo, tags);
    }

    public static WordEntity fromWord(Word word) {

        return new WordEntity(
                word.getWordOrigin(),
                word.getWordTranslate(),
                String.join(";", word.getWordInfo()),
                String.join(";", word.getTags())
        );
    }
}
