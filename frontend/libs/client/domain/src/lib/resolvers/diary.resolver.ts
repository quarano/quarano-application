import { DiaryService } from '../data-access/diary.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { DiaryDto } from '../model/diary-entry';

@Injectable()
export class DiaryResolver implements Resolve<DiaryDto> {
  constructor(private diaryService: DiaryService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<DiaryDto> {
    return this.diaryService.getDiary();
  }
}
