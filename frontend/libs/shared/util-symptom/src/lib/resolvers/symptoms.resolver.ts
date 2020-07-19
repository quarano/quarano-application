import { SymptomEntityService } from './../data-access/symptom-entity.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { SymptomDto } from '../model/symptom';

@Injectable()
export class SymptomsResolver implements Resolve<SymptomDto[]> {
  constructor(private entityService: SymptomEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<SymptomDto[]> {
    return this.entityService.getAll();
  }
}
