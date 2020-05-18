import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import {EncounterEntry} from '../models/encounter';
import {EnrollmentService} from '../services/enrollment.service';

@Injectable()
export class EncountersResolver implements Resolve<EncounterEntry[]> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<EncounterEntry[]> {
    return this.enrollmentService.getEncounters();
  }
}
