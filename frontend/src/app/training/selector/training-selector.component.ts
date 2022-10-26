import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { TrainingProvider } from '../training-provider.service'

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-training-selector',
  templateUrl: './training-selector.component.html',
  styleUrls: [ './training-selector.component.css' ]
})
export class TrainingSelectorComponent implements OnInit {

  private API_URL = environment.API_URL;
  private API_WORD_TAGS_URL = 'api/v1/word/tags';

  tagsArray: string[];
  selectedTag: string;
  allTypes = ["TAGGED", "REPEAT", "RANDOM"];
  selectedType: string;
  size: number;

  constructor(
    private trainingProvider: TrainingProvider,
    private router: Router,
    private http: HttpClient
  ) {
    this.selectedTag = '';
    this.selectedType = '';
    this.size = 0;
    this.tagsArray = new Array();

    this.http.get<string[]>(
      `${this.API_URL}/${this.API_WORD_TAGS_URL}`
    ).subscribe(tags => 
      {
        this.tagsArray.push(...tags);
        this.selectedTag = tags[0];
      }, 
      this.handleError
    )
  }

  ngOnInit(): void {
  }

  start(): void {
    this.trainingProvider.loadTraining([this.selectedTag], this.selectedType, this.size)
      .then(res => this.router.navigate(['/process']));
  }

  clear(): void {
    this.size = 0;
    this.selectedType = '';
    this.tagsArray = new Array();
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
     private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
  
        // TODO: send the error to remote logging infrastructure
        console.error(error); // log to console instead
  
        // Let the app keep running by returning an empty result.
        return of(result as T);
      };
    }
}
