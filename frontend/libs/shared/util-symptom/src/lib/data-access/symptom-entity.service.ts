import { SymptomDto } from './../model/symptom';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { Injectable } from '@angular/core';

export const SYMPTOM_FEATURE_KEY = 'Symptom';

@Injectable()
export class SymptomEntityService extends EntityCollectionServiceBase<SymptomDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(SYMPTOM_FEATURE_KEY, serviceElementsFactory);
  }
}
