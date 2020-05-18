import { EnrollmentService } from '@services/enrollment.service';
import { QuestionnaireDto } from '@models/first-query';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class MyFirstQueryResolver implements Resolve<QuestionnaireDto> {
  constructor(private enrollmentService: EnrollmentService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<QuestionnaireDto> {
    return this.enrollmentService.getQuestionnaire();
  }
}
