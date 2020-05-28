import { ContactCaseService } from './../services/contact-case.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ActionListItemDto } from '@qro/health-department/domain';

@Injectable()
export class ContactCaseActionListResolver implements Resolve<ActionListItemDto[]> {
  constructor(private apiService: ContactCaseService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ActionListItemDto[]> {
    return this.apiService.getActionList();
  }
}
