import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyFirstQueryResolver } from './resolvers/my-first-query.resolver';
import { EncountersResolver } from './resolvers/encounters.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [MyFirstQueryResolver, EncountersResolver],
})
export class ClientEnrollmentDomainModule {}
