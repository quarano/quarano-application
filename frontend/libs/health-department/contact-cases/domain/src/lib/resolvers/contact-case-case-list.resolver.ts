import { ContactCaseService } from './../services/contact-case.service';
import { CaseListItemDto } from '@qro/health-department/domain';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class ContactCaseCaseListResolver implements Resolve<CaseListItemDto[]> {
  constructor(private apiService: ContactCaseService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<CaseListItemDto[]> {
    return this.apiService.getCaseList();
  }
}
