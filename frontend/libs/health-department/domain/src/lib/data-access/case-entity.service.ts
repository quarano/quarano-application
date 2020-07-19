import { CaseDto } from '../model/case';
import { Injectable } from '@angular/core';
import { EntityCollectionServiceElementsFactory, EntityCollectionServiceBase } from '@ngrx/data';

export const CASE_FEATURE_KEY = 'Case';

@Injectable()
export class CaseEntityService extends EntityCollectionServiceBase<CaseDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }
}
