import { ContactPersonService } from '../data-access/contact-person.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ContactPersonDto } from '../model/contact-person';

@Injectable()
export class ContactPersonsResolver implements Resolve<ContactPersonDto[]> {
  constructor(private contactPersonService: ContactPersonService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ContactPersonDto[]> {
    return this.contactPersonService.getContactPersons();
  }
}
