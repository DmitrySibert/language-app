import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { Training } from '../training';

import { environment } from '../../environments/environment';


@Injectable({ providedIn: 'root' })
export class TrainingProvider {

  private API_URL = environment.API_URL;
  private API_TRAINING_URL = 'api/v1/training';
  private currentTraining: Observable<Training> | null;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) {
      this.currentTraining = null;
      console.log("url: " + this.API_URL);
      
    }

  loadTraining(tags: string[], type: string): void {
    let params = new HttpParams();
    tags.forEach(tag => {
      params = params.append('tags', tag);
    });
    params = params.append('type', type);

    this.currentTraining = this.http.get<Training>(
      `${this.API_URL}/${this.API_TRAINING_URL}`, {
        params: params
      })
  }

  getCurrentTraining(): Observable<Training> | null {
    return this.currentTraining;
  }

  completeTraining(training: Training): void {
      this.http.post(`${this.API_URL}/${this.API_TRAINING_URL}`, training).subscribe();
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