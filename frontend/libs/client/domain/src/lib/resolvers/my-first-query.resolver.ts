import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { EnrollmentService } from '../services/enrollment.service';
import { QuestionnaireDto } from '@qro/shared/util-data-access';

@Injectable()
export class MyFirstQueryResolver implements Resolve<QuestionnaireDto> {
  constructor(private enrollmentService: EnrollmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<QuestionnaireDto> {
    return this.enrollmentService.getQuestionnaire();
  }
}
