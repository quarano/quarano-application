import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonsResolver } from './resolvers/contact-persons.resolver';
import { ContactPersonResolver } from './resolvers/contact-person.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [ContactPersonsResolver, ContactPersonResolver],
})
export class ClientContactPersonsDomainModule {}
