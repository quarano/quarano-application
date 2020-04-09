import { UserService } from './../services/user.service';
import { FirstQuery } from './../models/first-query';
import { ApiService } from '../services/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyFirstQueryResolver implements Resolve<FirstQuery> {
  constructor(private apiService: ApiService, private userService: UserService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<FirstQuery> {
    // ToDo: Api Call, sobald Endpunkt vorliegt
    return of(
      {
        min15MinutesContactWithC19Pat: false,
        nursingActionOnC19Pat: null,
        directContactWithLiquidsOfC19Pat: null,
        flightPassengerWithCloseRowC19Pat: false,
        flightAsCrewMemberWithC19Pat: null,
        belongToMedicalStaff: null,
        belongToNursingStaff: true,
        belongToLaboratoryStaff: null,
        familyMember: null,
        otherContactType: 'Sehr viel Text hier',
        dayOfFirstSymptoms: null,
        hasSymptoms: null
      });
  }
}
