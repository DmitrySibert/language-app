import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from "./user";


@Injectable({ providedIn: 'root' })
export class UserSession {

  private user: User;

  constructor(
    private http: HttpClient) {
      this.user = new User('unauthorized');
    }

  setUser(user: User): void {
    this.user = user;
  }  

  getUser(): User {
    return this.user;
  }  
}
