import { Client } from './../models/client';
import { EnrollmentService } from '../services/enrollment.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyClientDataResolver implements Resolve<Client> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<Client> {
    return this.enrollmentService.getPersonalDetails();
  }
}
