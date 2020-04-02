import { SymptomDto } from './../models/symptom';
import { ApiService } from './../services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class SymptomsResolver implements Resolve<SymptomDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<SymptomDto[]> {
    return this.apiService.getSymptoms();
  }
}
