import { EnrollmentService } from './../data-access/enrollment.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ClientDto } from '@qro/auth/api';

@Injectable()
export class EnrollmentProfileResolver implements Resolve<ClientDto> {
  constructor(private enrollmentService: EnrollmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ClientDto> {
    return this.enrollmentService.getPersonalDetails();
  }
}
