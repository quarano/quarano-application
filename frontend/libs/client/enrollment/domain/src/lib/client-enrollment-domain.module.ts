import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsEnrolledClientDirective } from './directives/is-enrolled-client.directive';
import { MyFirstQueryResolver } from './resolvers/my-first-query.resolver';
import { EncountersResolver } from './resolvers/encounters.resolver';

@NgModule({
  declarations: [IsEnrolledClientDirective],
  imports: [CommonModule],
  exports: [IsEnrolledClientDirective],
  providers: [MyFirstQueryResolver, EncountersResolver],
})
export class ClientEnrollmentDomainModule {}
