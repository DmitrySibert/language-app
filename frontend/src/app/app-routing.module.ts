import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TrainingSelectorComponent } from './training/selector/training-selector.component';
import { AppComponent } from './app.component';
import { TrainingProcessorComponent } from './training/processor/training-processor.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'process', component: TrainingProcessorComponent },
  { path: 'select', component: TrainingSelectorComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
