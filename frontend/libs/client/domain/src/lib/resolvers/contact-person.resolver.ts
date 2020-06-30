import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ContactPersonDto } from '../model/contact-person';
import { ContactPersonService } from '../data-access/contact-person.service';

@Injectable()
export class ContactPersonResolver implements Resolve<ContactPersonDto> {
  constructor(private contactPersonService: ContactPersonService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ContactPersonDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.contactPersonService.getContactPerson(id);
    } else {
      return of({
        id: null,
        firstName: null,
        lastName: null,
        zipCode: null,
        email: null,
        phone: null,
        mobilePhone: null,
        street: null,
        houseNumber: null,
        city: null,
        remark: null,
        isHealthStaff: null,
        hasPreExistingConditions: null,
        isSenior: null,
        identificationHint: null,
        _links: null,
      });
    }
  }
}
