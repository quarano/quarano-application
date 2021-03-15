import { LocationsResolver } from './resolvers/locations.resolver';
import { EnrollmentProfileResolver } from './resolvers/enrollment-profile.resolver';
import { ClientStoreModule } from './client-store.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileResolver } from './resolvers/profile.resolver';
import { DiaryDetailResolver } from './resolvers/diary-detail.resolver';
import { DiaryResolver } from './resolvers/diary.resolver';
import { MyFirstQueryResolver } from './resolvers/my-first-query.resolver';
import { EncountersResolver } from './resolvers/encounters.resolver';
import { ContactPersonResolver } from './resolvers/contact-person.resolver';
import { ContactPersonsResolver } from './resolvers/contact-persons.resolver';

@NgModule({
  imports: [CommonModule, ClientStoreModule],
  providers: [
    ProfileResolver,
    DiaryDetailResolver,
    DiaryResolver,
    MyFirstQueryResolver,
    EncountersResolver,
    ContactPersonsResolver,
    ContactPersonResolver,
    EnrollmentProfileResolver,
    LocationsResolver,
  ],
})
export class ClientDomainModule {}
