import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyClientDataResolver } from './resolvers/my-client-data.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [MyClientDataResolver],
})
export class ClientDomainModule {}
