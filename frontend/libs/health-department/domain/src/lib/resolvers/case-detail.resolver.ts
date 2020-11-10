import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { shareReplay, take } from 'rxjs/operators';
import { CaseDto } from '../model/case';
import { CaseEntityService } from '../data-access/case-entity.service';

@Injectable()
export class CaseDetailResolver implements Resolve<CaseDto> {
  constructor(private entityService: CaseEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseDto> {
    const id = route.paramMap.get('id');
    return this.entityService.getByKey(id);
  }
}
