import { EnrollmentService } from './../services/enrollment.service';
import { FirstQuery } from './../models/first-query';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyFirstQueryResolver implements Resolve<FirstQuery> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<FirstQuery> {
    return this.enrollmentService.getFirstQuery();
    // return of(
    //   {
    //     min15MinutesContactWithC19Pat: false,
    //     nursingActionOnC19Pat: null,
    //     directContactWithLiquidsOfC19Pat: null,
    //     flightPassengerWithCloseRowC19Pat: false,
    //     flightAsCrewMemberWithC19Pat: null,
    //     belongToMedicalStaff: null,
    //     belongToNursingStaff: true,
    //     belongToLaboratoryStaff: null,
    //     familyMember: null,
    //     otherContactType: 'Sehr viel Text hier',
    //     dayOfFirstSymptoms: null,
    //     hasSymptoms: null
    //   });
  }
}
