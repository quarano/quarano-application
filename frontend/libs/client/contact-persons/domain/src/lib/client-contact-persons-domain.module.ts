import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonsResolver } from './resolvers/contact-persons.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [ContactPersonsResolver],
})
export class ClientContactPersonsDomainModule {}
