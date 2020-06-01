import { API_URL } from '@qro/shared/util';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ContactPersonDto, ContactPersonModifyDto } from '../models/contact-person';
import { share } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ContactPersonService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getContactPersons(): Observable<ContactPersonDto[]> {
    return this.httpClient.get<ContactPersonDto[]>(`${this.apiUrl}/api/contacts`).pipe(share());
  }

  getContactPerson(id: string): Observable<ContactPersonDto> {
    return this.httpClient.get<ContactPersonDto>(`${this.apiUrl}/api/contacts/${id}`).pipe(share());
  }

  createContactPerson(contactPerson: ContactPersonModifyDto): Observable<ContactPersonDto> {
    return this.httpClient.post<ContactPersonDto>(`${this.apiUrl}/api/contacts`, contactPerson);
  }

  modifyContactPerson(contactPerson: ContactPersonModifyDto, id: string) {
    return this.httpClient.put(`${this.apiUrl}/api/contacts/${id}`, contactPerson);
  }
}
