import { ContactCaseService } from '../services/contact-case.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { CaseListItemDto } from '../models/case-list-item';

@Injectable()
export class ContactCaseCaseListResolver implements Resolve<CaseListItemDto[]> {
  constructor(private apiService: ContactCaseService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseListItemDto[]> {
    return this.apiService.getCaseList();
  }
}
