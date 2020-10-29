import { Injectable } from '@angular/core';
import { EntityActionOptions, EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { CaseDto } from '../..';
import { HealthDepartmentService } from './health-department.service';
import { Observable, of } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { BadRequestService } from '@qro/shared/ui-error';

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
  constructor(
    serviceElementsFactory: EntityCollectionServiceElementsFactory,
    private hdService: HealthDepartmentService
  ) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }

  public loadOneFromStore(id: string): Observable<CaseDto> {
    if (id) {
      return this.entities$.pipe(
        switchMap((entities) => {
          const loadedCase = entities.find((caseDto) => id === caseDto.caseId);
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
