import { ClientStore } from './../store/client-store.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { map } from 'rxjs/operators';

@Injectable()
export class EnrollmentStatusResolver implements Resolve<boolean> {
  constructor(private clientStore: ClientStore) {}

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.clientStore.getEnrollmentStatus().pipe(map((s) => !!s));
  }
}
