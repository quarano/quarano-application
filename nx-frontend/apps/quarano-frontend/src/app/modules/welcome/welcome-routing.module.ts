import { WelcomeComponent } from './welcome.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { LandingComponent } from './landing/landing.component';
import {IsAuthenticatedGuard} from '../../guards/is-authenticated.guard';

const routes: Routes = [
  { path: 'register/:clientcode', component: RegisterComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'landing/:usertype/:clientcode', component: LandingComponent },
  { path: 'landing', component: LandingComponent },
  { path: '', component: WelcomeComponent, canActivate: [IsAuthenticatedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule { }
