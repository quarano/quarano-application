import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL, SelfLink } from '@qro/shared/util-data-access';
import { shareReplay } from 'rxjs/operators';
import { StoredContactLocationDto, TransientContactLocationDto } from '../..';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ContactLocationService {
  private static test: StoredContactLocationDto[] = [
    {
      id: '1',
      name: 'Test',
      street: 'Abc',
      houseNumber: '123',
      zipCode: '456',
      city: 'Mannheim',
      contactPerson: 'Abc Def',
      startTime: '11:00',
      endTime: '12:00',
      notes: 'äüö',
      _links: null,
    },
  ];

  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  // TODO for backend impl: location is usually connected to a certain day, the api should be differ from contact person
  getContactLocations(): Observable<StoredContactLocationDto[]> {
    // return this.httpClient.get<StoredContactLocationDto[]>(`${this.apiUrl}/api/locations`).pipe(shareReplay());
    return of(ContactLocationService.test);
  }

  getContactLocation(id: string): Observable<StoredContactLocationDto> {
    // return this.httpClient.get<StoredContactLocationDto>(`${this.apiUrl}/api/locations/${id}`).pipe(shareReplay());
    return of(ContactLocationService.test[0]);
  }

  createContactLocation(newLocation: TransientContactLocationDto): Observable<StoredContactLocationDto> {
    /*return this.httpClient
      .post<StoredContactLocationDto>(`${this.apiUrl}/api/locations`, newLocation)
      .pipe(shareReplay());*/
    return of(ContactLocationService.test[0]);
  }
}
