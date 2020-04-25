import { ActionListItemDto } from '@models/action';
import { ApiService } from '@services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class ActionsResolver implements Resolve<ActionListItemDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ActionListItemDto[]> {
    return this.apiService.getAllActions();
  }
}
