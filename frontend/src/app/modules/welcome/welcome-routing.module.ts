import { WelcomeComponent } from './welcome.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { IsAuthenticatedGuard } from '@guards/is-authenticated.guard';

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'register/:clientcode', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: '', component: WelcomeComponent, canActivate: [IsAuthenticatedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule { }
