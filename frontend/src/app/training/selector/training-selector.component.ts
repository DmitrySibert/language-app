import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

import { TrainingProvider } from '../training-provider.service'

@Component({
  selector: 'app-training-selector',
  templateUrl: './training-selector.component.html',
  styleUrls: [ './training-selector.component.css' ]
})
export class TrainingSelectorComponent implements OnInit {

  tags: string;
  type: string;
  counter: number;

  constructor(
    private trainingProvider: TrainingProvider,
    private location: Location,
    private router: Router  
  ) {
    this.type = '';
    this.tags = '';
    this.counter = 0;
  }

  ngOnInit(): void {
  }

  start(): void {
    let tagsArray = this.tags.split(',');
    this.trainingProvider.loadTraining(tagsArray, this.type);
    this.router.navigate(['/process']);
  }

  clear(): void {
    this.type = '';
    this.tags = '';
  }
}