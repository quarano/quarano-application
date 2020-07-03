import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { SymptomDto } from '../model/symptom';
import { SymptomService } from '../data-access/symptom.service';

@Injectable()
export class SymptomsResolver implements Resolve<SymptomDto[]> {
  constructor(private symptomService: SymptomService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<SymptomDto[]> {
    return this.symptomService.getSymptoms();
  }
}
