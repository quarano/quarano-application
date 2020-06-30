import { IndexCaseService } from '../data-access/index-case.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ActionListItemDto } from '../model/action-list-item';

@Injectable()
export class IndexCaseActionListResolver implements Resolve<ActionListItemDto[]> {
  constructor(private apiService: IndexCaseService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ActionListItemDto[]> {
    return this.apiService.getActionList();
  }
}
