import { Client } from './../models/client';
import { UserService } from '../services/user.service';
import { FirstQuery } from '../models/first-query';
import { ApiService } from '../services/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyClientDataResolver implements Resolve<Client> {
  constructor(private apiService: ApiService, private userService: UserService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<Client> {
    const clientCode = this.userService.user.clientCode;

    if (clientCode) {
      // ToDo: Api Call, sobald Endpunkt vorliegt
      return this.apiService.getClientByCode(clientCode);
    } else {
      return of(
        {
          surename: null,
          firstname: null,
          dateOfBirth: null,
          street: null,
          houseNumber: null,
          zipCode: null,
          city: null,
          email: null,
          phone: null,
          mobilePhone: null,
          infected: null,
          healthDepartmentId: null,
          clientId: null,
          clientCode: null
        });
    }
  }
}
