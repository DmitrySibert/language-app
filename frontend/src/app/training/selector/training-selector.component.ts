import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { TrainingProvider } from '../training-provider.service'

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-training-selector',
  templateUrl: './training-selector.component.html',
  styleUrls: [ './training-selector.component.css' ]
})
export class TrainingSelectorComponent implements OnInit {

  private API_WORD_ORIGINS_URL = 'api/v1/word/tags';
  private API_URL = environment.API_URL;

  // tags: string;
  // allTags: string;
  tagsArray: string[];
  selectedTag: string;
  allTypes = ["TAGGED", "REPEAT", "RANDOM"];
  selectedType: string;
  // type: string;
  size: number;

  constructor(
    private trainingProvider: TrainingProvider,
    private location: Location,
    private router: Router,
    private http: HttpClient
  ) {
    // this.type = '';
    // this.tags = '';
    // this.allTags = '';
    this.selectedTag = '';
    this.selectedType = '';
    this.size = 0;
    this.tagsArray = new Array();

    this.http.get<string[]>(
      `${this.API_URL}/${this.API_WORD_ORIGINS_URL}`
    ).subscribe(res => {
      this.tagsArray.push(...res);
    })
  }

  ngOnInit(): void {
  }

  start(): void {
    // let tagsArray = this.tags.split(',');
    this.trainingProvider.loadTraining([this.selectedTag], this.selectedType, this.size)
      .then(res => this.router.navigate(['/process']));
  }

  clear(): void {
    // this.type = '';
    // this.tags = '';
    // this.allTags = '';
    this.size = 0;
    this.selectedType = '';
    this.tagsArray = new Array();
  }
}
