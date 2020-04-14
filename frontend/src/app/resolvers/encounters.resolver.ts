import { EnrollmentService } from './../services/enrollment.service';
import { EncounterDto } from './../models/encounter';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class EncountersResolver implements Resolve<EncounterDto[]> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<EncounterDto[]> {
    return this.enrollmentService.getEncounters();
  }
}
