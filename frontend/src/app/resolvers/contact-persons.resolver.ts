import { ContactPersonDto } from './../models/contact-person';
import { SymptomDto } from '../models/symptom';
import { ApiService } from '../services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class ContactPersonsResolver implements Resolve<ContactPersonDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ContactPersonDto[]> {
    return this.apiService.getContactPersons();
  }
}
