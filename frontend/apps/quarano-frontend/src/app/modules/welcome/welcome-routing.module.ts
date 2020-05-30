import { WelcomeComponent } from './welcome.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LandingComponent } from './landing/landing.component';
import { IsAuthenticatedGuard } from '@qro/auth/api';

const routes: Routes = [
  { path: 'register/:clientcode', component: RegisterComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'landing/:usertype/:clientcode', component: LandingComponent },
  { path: 'landing', component: LandingComponent },
  { path: '', component: WelcomeComponent, canActivate: [IsAuthenticatedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule { }
