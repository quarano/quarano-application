import { LocationDto } from './../model/location';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { API_URL } from '@qro/shared/util-data-access';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getLocations(): Observable<LocationDto[]> {
    return this.httpClient.get<LocationDto[]>(`${this.apiUrl}/locations`).pipe(shareReplay());
  }

  createLocation(location: LocationDto): Observable<LocationDto> {
    return this.httpClient.post<LocationDto>(`${this.apiUrl}/locations`, location).pipe(shareReplay());
  }

  modifyLocation(location: LocationDto, id: string) {
    return this.httpClient.put(`${this.apiUrl}/locations/${id}`, location).pipe(shareReplay());
  }
}
