import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { TrainingProvider } from '../training-provider.service'
import { Training } from '../../training';
import { Word } from '../../word';

@Component({
  selector: 'app-training-processor',
  templateUrl: './training-processor.component.html',
  styleUrls: [ './training-processor.component.css' ]
})
export class TrainingProcessorComponent implements OnInit {

  private training: Training | null;
  public isShowInfo: boolean;
  public word: Word;
  private curWordIndex: number;

  constructor(
    private trainingProvider: TrainingProvider,
    private location: Location
  ) {
    this.training = null;
    trainingProvider.getCurrentTraining()?.subscribe(tr => {
      this.training = tr;
      this.word = this.training.words[this.curWordIndex];
    })
    this.curWordIndex = 0;
    this.word = { 
      wordOrigin: '', wordTranslate: '', wordInfo: [], tags: [],
    };
    this.isShowInfo = false;
  }

  ngOnInit(): void {
  }

  approve(): void {
    this.training?.approvedWords.push(this.word);
    ++this.curWordIndex;
    if (this.training) {
      this.word = this.training.words[this.curWordIndex];
    }
    this.isShowInfo = false;
  }

  repeat(): void {
    this.training?.wordsForRepetition.push(this.word);
    ++this.curWordIndex;
    if (this.training) {
      this.word = this.training.words[this.curWordIndex];
    }
    this.isShowInfo = false;
  }

  showInfo(): void {
    this.isShowInfo = true;
  }

  complete(): void {
    if (this.training) {
      this.trainingProvider.completeTraining(this.training);
    }
    this.location.back();
  }
}