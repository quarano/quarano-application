import { ClientDto } from './../models/client';
import { EnrollmentService } from '../services/enrollment.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyClientDataResolver implements Resolve<ClientDto> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ClientDto> {
    return this.enrollmentService.getPersonalDetails();
  }
}
