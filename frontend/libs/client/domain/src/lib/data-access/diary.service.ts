import { API_URL } from '@qro/shared/util-data-access';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DiaryEntryDto, DiaryDto, DiaryEntryModifyDto } from '../model/diary-entry';
import { share, map, shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class DiaryService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getDiaryEntry(id: string): Observable<DiaryEntryDto> {
    return this.httpClient.get<DiaryEntryDto>(`${this.apiUrl}/api/diary/${id}`).pipe(
      shareReplay(),
      map((entry) => {
        entry.characteristicSymptoms = entry.symptoms.filter((s) => s.characteristic);
        entry.nonCharacteristicSymptoms = entry.symptoms.filter((s) => !s.characteristic);
        entry.date = this.getDate(entry.date);
        return entry;
      })
    );
  }

  getDiary(): Observable<DiaryDto> {
    return this.httpClient.get<DiaryDto>(`${this.apiUrl}/api/diary`).pipe(shareReplay());
  }

  createDiaryEntry(diaryEntry: DiaryEntryModifyDto): Observable<DiaryEntryDto> {
    return this.httpClient.post<DiaryEntryDto>(`${this.apiUrl}/api/diary`, diaryEntry).pipe(
      shareReplay(),
      map((entry) => {
        entry.date = this.getDate(entry.date);
        return entry;
      })
    );
  }

  modifyDiaryEntry(diaryEntry: DiaryEntryModifyDto) {
    return this.httpClient.put(`${this.apiUrl}/api/diary/${diaryEntry.id}`, diaryEntry).pipe(shareReplay());
  }

  private getDate(date: Date): Date {
    return new Date(date + 'Z');
  }
}
