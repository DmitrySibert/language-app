package com.dsib.language.core.training;

import com.dsib.language.core.word.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Training {

    private List<Word> words;
    private int curWordIdx;
    private List<Word> wordsForRepetition;
    private List<Word> approvedWords;

    public Training(List<Word> words) {
        this.words = new ArrayList<>(words);
        wordsForRepetition = new LinkedList<>();
        approvedWords = new LinkedList<>();
    }

  public Training() {
  }

    public void start() {
        curWordIdx = 0;
        wordsForRepetition.clear();
        approvedWords.clear();
    }

    public Word getCurrent() {
        return words.get(curWordIdx);
    }

    public boolean moveNext() {
        if (isEnd()) {
            return false;
        }
        curWordIdx++;
        return true;
    }

    public boolean isEnd() {
        if (words.isEmpty()) {
            return true;
        }
        return curWordIdx == (words.size() - 1);
    }

    public void addCurrentWordToRepeat() {
        wordsForRepetition.add(words.get(curWordIdx));
    }

    public void approveCurrentWord() {
        approvedWords.add(words.get(curWordIdx));
    }

    public List<Word> getWordsForRepetition() {
        return wordsForRepetition;
    }

    public List<Word> getApprovedWords() {
        return approvedWords;
    }


    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public int getCurWordIdx() {
        return curWordIdx;
    }

    public void setCurWordIdx(int curWordIdx) {
        this.curWordIdx = curWordIdx;
    }

    public void setWordsForRepetition(List<Word> wordsForRepetition) {
        this.wordsForRepetition = wordsForRepetition;
    }

    public void setApprovedWords(List<Word> approvedWords) {
        this.approvedWords = approvedWords;
    }
}
