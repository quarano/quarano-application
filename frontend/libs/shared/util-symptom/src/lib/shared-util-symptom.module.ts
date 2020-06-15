import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SymptomsResolver } from './resolvers/symptoms.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [SymptomsResolver],
})
export class SharedUtilSymptomModule {}
