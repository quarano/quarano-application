import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { CaseDto } from '../..';
import { HealthDepartmentService } from './health-department.service';
import { Observable, of } from 'rxjs';
import { first, switchMap } from 'rxjs/operators';

export const CASE_FEATURE_KEY = 'Case';

@Injectable()
export class CaseEntityService extends EntityCollectionServiceBase<CaseDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }

  public get emptyCase(): CaseDto {
    return {
      firstName: null,
      lastName: null,
      quarantineEndDate: null,
      quarantineStartDate: null,
      dateOfBirth: null,
      infected: null,
      caseTypeLabel: null,
      extReferenceNumber: null,
      contactCount: null,
      originCases: [],
      _embedded: { originCases: [] },
    };
  }
}
