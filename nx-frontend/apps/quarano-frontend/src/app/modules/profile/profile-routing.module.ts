import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { ProfileComponent } from './profile.component';
import {MyClientDataResolver} from '../../resolvers/my-client-data.resolver';

const routes: Routes = [
  {
    path: '',
    component: ProfileComponent,
    resolve:
    {
      clientData: MyClientDataResolver
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
