import { API_URL } from '@qro/shared/util';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ContactPersonDto } from '../models/contact-person';
import { share } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ContactPersonService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getContactPersons(): Observable<ContactPersonDto[]> {
    return this.httpClient.get<ContactPersonDto[]>(`${this.apiUrl}/api/contacts`).pipe(share());
  }
}
