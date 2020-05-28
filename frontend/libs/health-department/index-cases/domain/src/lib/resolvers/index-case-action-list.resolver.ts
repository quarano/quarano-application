import { IndexCaseService } from './../services/index-case.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ActionListItemDto } from '@qro/health-department/domain';

@Injectable()
export class IndexCaseActionListResolver implements Resolve<ActionListItemDto[]> {
  constructor(private apiService: IndexCaseService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ActionListItemDto[]> {
    return this.apiService.getActionList();
  }
}
