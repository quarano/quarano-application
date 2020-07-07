import { Injectable } from '@angular/core';
import {
  DefaultDataServiceConfig,
  EntityActionOptions,
  EntityCollectionServiceBase,
  EntityCollectionServiceElementsFactory,
} from '@ngrx/data';
import { CaseDetailDto } from '@qro/health-department/domain';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CaseService extends EntityCollectionServiceBase<CaseDetailDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Case', serviceElementsFactory);
  }

  getAll(options?: EntityActionOptions): Observable<CaseDetailDto[]> {
    // @todo SC CORE-341
    // return super.getAll(options); Standard Methode von Data
    // this.addManyToCache() so können wir den state befüllen wenn wir die API nicht pro service gemappt bekommen
    // api kann im app module gesetzt werden
    return null;
  }
}
