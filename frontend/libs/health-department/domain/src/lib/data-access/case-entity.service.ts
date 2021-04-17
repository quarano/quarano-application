import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { CaseDto } from '../..';
import { HealthDepartmentService } from './health-department.service';
import { Observable, of } from 'rxjs';
import { first, switchMap } from 'rxjs/operators';

export const CASE_FEATURE_KEY = 'Case';

const emptyCase = {
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

@Injectable()
export class CaseEntityService extends EntityCollectionServiceBase<CaseDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }

  public loadOneFromStore(id: string): Observable<CaseDto> {
    if (id) {
      return this.entityMap$.pipe(
        first(),
        switchMap((entities) => {
          const loadedCase = entities[id];
          if (loadedCase && loadedCase?.comments) {
            return of(loadedCase);
          } else {
            return this.getByKey(id);
          }
        })
      );
    } else {
      return of(emptyCase);
    }
  }
}
