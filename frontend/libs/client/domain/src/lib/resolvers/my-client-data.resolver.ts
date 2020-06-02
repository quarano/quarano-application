import { ClientService } from './../services/client.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ClientDto } from '../models/client';

@Injectable()
export class MyClientDataResolver implements Resolve<ClientDto> {
  constructor(private clientService: ClientService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ClientDto> {
    return this.clientService.getPersonalDetails();
  }
}
