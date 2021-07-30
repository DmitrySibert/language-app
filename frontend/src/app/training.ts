import { Word } from "./word";

export interface Training {
    words: Word[];
    curWordIdx: number;
    wordsForRepetition: Word[];
    approvedWords: Word[];
}