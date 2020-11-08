import { ProfileService } from '../data-access/profile.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ClientDto } from '@qro/auth/api';

@Injectable()
export class ProfileResolver implements Resolve<ClientDto> {
  constructor(private profileService: ProfileService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ClientDto> {
    return this.profileService.getPersonalDetails();
  }
}
