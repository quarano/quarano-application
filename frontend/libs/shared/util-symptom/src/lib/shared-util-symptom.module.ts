import { SymptomService } from './data-access/symptom.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SymptomsResolver } from './resolvers/symptoms.resolver';
import { EffectsModule } from '@ngrx/effects';
import { SymptomEffects } from './store/symptom.effects';
import { StoreModule } from '@ngrx/store';
import { symptomReducer, SYMPTOM_FEATURE_KEY } from './reducers';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(SYMPTOM_FEATURE_KEY, symptomReducer),
    EffectsModule.forFeature([SymptomEffects]),
  ],
  providers: [SymptomService, SymptomsResolver],
})
export class SharedUtilSymptomModule {}
