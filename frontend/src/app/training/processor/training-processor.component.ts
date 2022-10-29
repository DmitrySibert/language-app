import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { TrainingProvider } from '../training-provider.service'
import { UserSession } from '../../login/user-session.service'
import { Training } from '../model/training';
import { Word } from '../model/word';

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
  private wordOrigins: Map<string, Word>;

  constructor(
    private trainingProvider: TrainingProvider,
    private userSession: UserSession,
    private location: Location
  ) {
    this.word = {
      wordOrigin: '', wordTranslate: '', wordInfo: [], tags: [],
    };
    this.training = null;
    this.wordOrigins = trainingProvider.getCurrentTrainingWordOrigins();
    this.curWordIndex = 0;
    this.training = trainingProvider.getCurrentTraining();
    if (this.wordOrigins.get(this.training!.trainingSet.words[this.curWordIndex])) {
      this.word = this.wordOrigins.get(this.training!.trainingSet.words[this.curWordIndex])!;
    }
    
    this.isShowInfo = false;
  }

  ngOnInit(): void {
  }

  approve(): void {
    this.training?.trainingSet.approved.push(this.word.wordOrigin);
    ++this.curWordIndex;
    if (this.training) {
      this.word = this.getCurWord();
    }
    this.isShowInfo = false;
  }

  repeat(): void {
    this.training?.trainingSet.failed.push(this.word.wordOrigin);
    ++this.curWordIndex;
    if (this.training) {
      this.word = this.getCurWord();
    }
    this.isShowInfo = false;
  }

  getCurWord(): Word {
    return this.wordOrigins.get(this.training!.trainingSet.words[this.curWordIndex])!;
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
