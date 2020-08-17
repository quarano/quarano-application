import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { CaseDto } from '../..';

export const CASE_FEATURE_KEY = 'Case';

@Injectable()
export class CaseEntityService extends EntityCollectionServiceBase<CaseDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }
}
