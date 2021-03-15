import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { LocationDto } from '../model/location';
import { LocationService } from '../data-access/location.service';

@Injectable()
export class LocationsResolver implements Resolve<LocationDto[]> {
  constructor(private locationService: LocationService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<LocationDto[]> {
    return this.locationService.getLocations();
  }
}
