import { Routes, RouterModule } from '@angular/router';
import { MyClientDataResolver } from '../resolvers/my-client-data.resolver';
import { NgModule } from '@angular/core';
import { ProfileComponent } from './profile.component';

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
