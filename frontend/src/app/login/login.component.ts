import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';

import { UserSession } from './user-session.service';
import { User } from "./user";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;

  constructor(
    private router: Router,
    private userSession: UserSession
  ) { 
    this.username = ''
  }

  ngOnInit(): void {
  }

  login(): void {
    this.userSession.setUser(new User(this.username));
    this.router.navigate(['/select'])
  }
}
