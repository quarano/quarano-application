import { DiaryResolver } from './resolvers/diary.resolver';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiaryDetailResolver } from './resolvers/diary-detail.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [DiaryDetailResolver, DiaryResolver],
})
export class ClientDiaryDomainModule {}
