import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Router } from '@angular/router';

import { environment } from '../../environments/environment';

import { UserSession } from '../login/user-session.service';

@Component({
  selector: 'app-dictionary',
  templateUrl: './dictionary.component.html',
  styleUrls: ['./dictionary.component.css']
})
export class DictionaryComponent implements OnInit {

  private API_URL = environment.API_URL;
  private API_TRAINING_URL = 'api/v1/dictionary/files/upload';

  private file1: File | undefined;
  private file2: File | undefined;
  private file3: File | undefined;

  constructor(
    private router: Router,
    private userSession: UserSession,
    private http: HttpClient
  ) { 
  }

  ngOnInit(): void {
  }

  onFile1Selected(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    if (element.files) {
      this.file1 = element.files[0];
    }
  }


  onFile2Selected(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    if (element.files) {
      this.file2 = element.files[0];
    }
  }

  onFile3Selected(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    if (element.files) {
      this.file3 = element.files[0];
    }
  }


  upload(): void {
    const dictionariesMultiPartForm = new FormData();
    if (this.file1) {
      dictionariesMultiPartForm.append("dictionaries", this.file1);
    }
    if (this.file2) {
      dictionariesMultiPartForm.append("dictionaries", this.file2);
    }
    if (this.file3) {
      dictionariesMultiPartForm.append("dictionaries", this.file3);
    }

    let headers = new HttpHeaders();
    headers = headers.append("X-USER-ID", this.userSession.getUser().username);
    this.http.post(`${this.API_URL}/${this.API_TRAINING_URL}`, dictionariesMultiPartForm, {
      headers: headers
    })
      .subscribe();
  }

  //Routing
  switchToTraining(): void {
    this.router.navigate(['/select'])
  }

}
