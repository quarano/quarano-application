import { EncountersResolver } from '@resolvers/encounters.resolver';
import { MyClientDataResolver } from '@resolvers/my-client-data.resolver';
import { MyFirstQueryResolver } from '@resolvers/my-first-query.resolver';
import { BasicDataComponent } from './basic-data.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContactPersonsResolver } from '@resolvers/contact-persons.resolver';
import {SymptomsResolver} from '@resolvers/symptoms.resolver';

const routes: Routes = [
  {
    path: '',
    component: BasicDataComponent,
    resolve:
    {
      symptoms: SymptomsResolver,
      contactPersons: ContactPersonsResolver,
      firstQuery: MyFirstQueryResolver,
      clientData: MyClientDataResolver,
      encounters: EncountersResolver
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BasicDataRoutingModule { }
