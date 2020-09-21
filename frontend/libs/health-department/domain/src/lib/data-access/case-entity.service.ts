import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { CaseDto, mapCaseIdToCaseEntity } from '../..';
import { Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { Dictionary } from '@ngrx/entity';
import { Store } from '@ngrx/store';

export const CASE_FEATURE_KEY = 'Case';

@Injectable()
export class CaseEntityService extends EntityCollectionServiceBase<CaseDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(CASE_FEATURE_KEY, serviceElementsFactory);
  }

  public loadOneFromStore(id: string): Observable<CaseDto> {
    return this.entities$.pipe(
      switchMap((entities) => {
        const caseDto = entities.find((caseDto) => id === caseDto.caseId);
        if (caseDto) {
          return of(caseDto);
        } else {
          return this.getByKey(id);
        }
      })
    );
  }
}
