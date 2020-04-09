import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CreateUserComponent} from './create-user/create-user.component';
import {RegisterComponent} from './register/register.component';
import {LoginComponent} from './login/login.component';
import {IsNotAuthenticatedGuard} from '../guards/is-not-authenticated.guard';


const routes: Routes = [
  { path: 'create-user', component: CreateUserComponent },
  { path: 'register', component: RegisterComponent, canActivate: [IsNotAuthenticatedGuard] },
  { path: 'login', component: LoginComponent, canActivate: [IsNotAuthenticatedGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule { }
