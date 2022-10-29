import { Component } from '@angular/core';
import { TrainingProvider } from './training/training-provider.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'language-app';

  constructor(
    private trainingProvider: TrainingProvider
  ) {}

  ngOnInit(): void {
  }
}
