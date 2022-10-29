import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UserSession } from '../login/user-session.service';
import { Training } from './model/training';
import { Word } from "./model/word";

import { environment } from '../../environments/environment';


@Injectable({ providedIn: 'root' })
export class TrainingProvider {

  private API_URL = environment.API_URL;
  private API_TRAINING_URL = 'api/v1/training';
  private API_WORD_ORIGINS_URL = 'api/v1/word';
  private currentTraining: Training | null;
  private wordOrigins: Map<string, Word>;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private userSession: UserSession) {
      this.currentTraining = null;
      this.wordOrigins = new Map<string, Word>();
      console.log("url: " + this.API_URL);
    }

  loadTraining(tags: string[], type: string, size: number): Promise<Boolean> {
    let params = new HttpParams();
    tags.forEach(tag => {
      params = params.append('tags', tag);
    });
    params = params.append('type', type);
    if (size > 0) {
      params = params.append('size', size);
    }
    let headers = new HttpHeaders();
    headers = headers.append("X-USER-ID", this.userSession.getUser().username);
    return this.http.get<Training>(
      `${this.API_URL}/${this.API_TRAINING_URL}`, {
        params: params,
        headers: headers
      })
      .toPromise()
      .then(newTraining => {
        this.currentTraining = newTraining;
        return this.getWords(this.currentTraining!.trainingSet.words)
          .toPromise()
          .then(words => {
            words.forEach(word => {
              this.wordOrigins.set(word.wordOrigin, word);
            })
            return true;
          })
      })
  }

  getWords(words: string[]): Observable<Word[]> {
    let wordOriginsParams = new HttpParams();
    words.forEach(wordOrigin => {
      wordOriginsParams = wordOriginsParams.append('origins', wordOrigin);
    });
    let headers = new HttpHeaders();
    headers = headers.append("X-USER-ID", this.userSession.getUser().username);
    return this.http.get<Word[]>(
      `${this.API_URL}/${this.API_WORD_ORIGINS_URL}`, {
        params: wordOriginsParams,
        headers: headers
      });
  }

  getCurrentTraining(): Training | null {
    return this.currentTraining;
  }

  getCurrentTrainingWordOrigins(): Map<string, Word> {
    return this.wordOrigins;
  }

  completeTraining(training: Training): void {
    let headers = new HttpHeaders();
    headers = headers.append("X-USER-ID", this.userSession.getUser().username);
      this.http.post(`${this.API_URL}/${this.API_TRAINING_URL}`, {
        id: training.id,
        wordsForRepetition: training.trainingSet.failed,
        approvedWords: training.trainingSet.approved
      }, {
        headers: headers
      }).subscribe();
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
