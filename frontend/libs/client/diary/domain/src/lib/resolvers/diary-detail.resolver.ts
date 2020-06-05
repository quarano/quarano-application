import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { DiaryEntryDto } from '../models/diary-entry';
import { DiaryService } from '../services/diary.service';

@Injectable()
export class DiaryDetailResolver implements Resolve<DiaryEntryDto> {
  constructor(private diaryService: DiaryService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<DiaryEntryDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.diaryService.getDiaryEntry(id);
    } else {
      return of({
        id: null,
        characteristicSymptoms: [],
        nonCharacteristicSymptoms: [],
        bodyTemperature: null,
        date: new Date(),
        symptoms: [],
        contacts: [],
        _links: null,
        reportedAt: null,
        slot: { date: null, timeOfDay: null },
      });
    }
  }
}
