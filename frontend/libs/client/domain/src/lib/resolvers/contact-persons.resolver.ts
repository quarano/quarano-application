import { ContactPersonService } from '../services/contact-person.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ContactPersonDto } from '../models/contact-person';

@Injectable()
export class ContactPersonsResolver implements Resolve<ContactPersonDto[]> {
  constructor(private contactPersonService: ContactPersonService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ContactPersonDto[]> {
    return this.contactPersonService.getContactPersons();
  }
}
