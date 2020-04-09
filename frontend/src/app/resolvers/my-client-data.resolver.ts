import { Client } from './../models/client';
import { UserService } from '../services/user.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyClientDataResolver implements Resolve<Client> {
  constructor(private userService: UserService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<Client> {
    return this.userService.client$;
  }
}
