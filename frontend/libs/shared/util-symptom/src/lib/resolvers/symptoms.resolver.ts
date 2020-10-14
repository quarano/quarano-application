import { SymptomEntityService } from './../data-access/symptom-entity.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { SymptomDto } from '../model/symptom';
import { first, switchMap } from 'rxjs/operators';

@Injectable()
export class SymptomsResolver implements Resolve<SymptomDto[] | boolean> {
  constructor(private entityService: SymptomEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<SymptomDto[] | boolean> {
    return this.entityService.loaded$.pipe(
      switchMap((loaded) => {
        if (!loaded) {
          return this.entityService.getAll();
        } else {
          return this.entityService.entities$;
        }
      }),
      first()
    );
  }
}
