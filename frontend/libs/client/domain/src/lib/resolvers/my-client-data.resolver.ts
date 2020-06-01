import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { EnrollmentService } from '../../../../enrollment/domain/src/lib/services/enrollment.service';
import { ClientDto } from '../models/client';

@Injectable()
export class MyClientDataResolver implements Resolve<ClientDto> {
  constructor(private enrollmentService: EnrollmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ClientDto> {
    return this.enrollmentService.getPersonalDetails();
  }
}
