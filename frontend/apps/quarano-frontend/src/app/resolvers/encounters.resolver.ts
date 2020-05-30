import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { EncounterEntry } from '../../../../../libs/client/domain/src/lib/models/encounter';
import { EnrollmentService } from '../../../../../libs/client/enrollment/domain/src/lib/services/enrollment.service';

@Injectable()
export class EncountersResolver implements Resolve<EncounterEntry[]> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<EncounterEntry[]> {
    return this.enrollmentService.getEncounters();
  }
}
