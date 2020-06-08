import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyClientDataResolver } from './resolvers/my-client-data.resolver';
import { DiaryDetailResolver } from './resolvers/diary-detail.resolver';
import { DiaryResolver } from './resolvers/diary.resolver';
import { MyFirstQueryResolver } from './resolvers/my-first-query.resolver';
import { EncountersResolver } from './resolvers/encounters.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [MyClientDataResolver, DiaryDetailResolver, DiaryResolver, MyFirstQueryResolver, EncountersResolver],
})
export class ClientDomainModule {}
