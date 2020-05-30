import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { QuestionnaireDto, EnrollmentService } from '@qro/client/enrollment/domain';

@Injectable()
export class MyFirstQueryResolver implements Resolve<QuestionnaireDto> {
  constructor(private enrollmentService: EnrollmentService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<QuestionnaireDto> {
    return this.enrollmentService.getQuestionnaire();
  }
}
