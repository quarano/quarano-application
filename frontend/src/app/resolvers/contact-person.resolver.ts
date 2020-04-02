import { ContactPersonDto } from './../models/contact-person';
import { ApiService } from '../services/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, ParamMap } from '@angular/router';
import { DiaryEntryDto } from '../models/diary-entry';
import { pipe } from 'rxjs';

@Injectable()
export class ContactPersonResolver implements Resolve<ContactPersonDto> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ContactPersonDto> {
    const id = Number(route.paramMap.get('id'));

    if (id) {
      return this.apiService.getContactPerson(id);
    } else {
      return of(
        {
          id: null,
          firstname: null,
          surename: null,
          zipCode: null,
          email: null,
          phone: null
        });
    }
  }
}
