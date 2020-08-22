import { ClientStoreModule } from './client-store.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyClientDataResolver } from './resolvers/my-client-data.resolver';
import { DiaryDetailResolver } from './resolvers/diary-detail.resolver';
import { DiaryResolver } from './resolvers/diary.resolver';
import { MyFirstQueryResolver } from './resolvers/my-first-query.resolver';
import { EncountersResolver } from './resolvers/encounters.resolver';
import { ContactPersonResolver } from './resolvers/contact-person.resolver';
import { ContactPersonsResolver } from './resolvers/contact-persons.resolver';

@NgModule({
  imports: [CommonModule, ClientStoreModule],
  providers: [
    MyClientDataResolver,
    DiaryDetailResolver,
    DiaryResolver,
    MyFirstQueryResolver,
    EncountersResolver,
    ContactPersonsResolver,
    ContactPersonResolver,
  ],
})
export class ClientDomainModule {}
