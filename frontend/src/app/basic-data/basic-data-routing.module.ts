import { MyClientDataResolver } from './../resolvers/my-client-data.resolver';
import { MyFirstQueryResolver } from './../resolvers/my-first-query.resolver';
import { BasicDataComponent } from './basic-data.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContactPersonsResolver } from '../resolvers/contact-persons.resolver';

const routes: Routes = [
  {
    path: '',
    component: BasicDataComponent,
    resolve:
    {
      contactPersons: ContactPersonsResolver,
      firstQuery: MyFirstQueryResolver,
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BasicDataRoutingModule { }
