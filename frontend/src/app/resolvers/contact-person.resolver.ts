import { ContactPersonDto } from './../models/contact-person';
import { ApiService } from '../services/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

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
          phone: null,
          mobilePhone: null,
          street: null,
          houseNumber: null,
          city: null,
          remark: null,
          isHealthStuff: null,
          hasPreExistingConditions: null,
          isSenior: null,
          identificationHint: null
        });
    }
  }
}
