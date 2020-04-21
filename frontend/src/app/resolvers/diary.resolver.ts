import { ApiService } from '@services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { DiaryDto } from '@models/diary-entry';

@Injectable()
export class DiaryResolver implements Resolve<DiaryDto> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<DiaryDto> {
    return this.apiService.getDiary();
  }
}
