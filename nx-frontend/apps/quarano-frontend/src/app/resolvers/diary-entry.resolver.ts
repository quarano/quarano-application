import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import {DiaryEntryDto} from '../models/diary-entry';
import {ApiService} from '../services/api.service';

@Injectable()
export class DiaryEntryResolver implements Resolve<DiaryEntryDto> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<DiaryEntryDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.apiService.getDiaryEntry(id);
    } else {
      return of(
        {
          id: null,
          characteristicSymptoms: [],
          nonCharacteristicSymptoms: [],
          bodyTemperature: null,
          date: new Date(),
          symptoms: [],
          contacts: [],
          _links: null,
          reportedAt: null,
          slot: { date: null, timeOfDay: null }
        });
    }
  }
}
