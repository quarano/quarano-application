import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { OccasionDto } from '../../../../domain/src/lib/model/occasion';
import { switchMap, take } from 'rxjs/operators';
import { HealthDepartmentService } from '@qro/health-department/domain';

@Injectable({
  providedIn: 'root',
})
export class OccasionService {
  constructor(private healthDepartmentService: HealthDepartmentService) {}

  getOccasions(): Observable<OccasionDto[]> {
    return this.healthDepartmentService
      .getOccasion()
      .pipe(switchMap((occasions) => of(occasions?._embedded?.occasions)));
  }

  saveOccasion(caseId: string, occasion: OccasionDto): Observable<any> {
    return this.healthDepartmentService.addOccasion(caseId, occasion);
  }

  editOccasion(caseId: string, occasion: OccasionDto): Observable<any> {
    return this.healthDepartmentService.editOccasion(caseId, occasion);
  }

  deleteOccasion(occasion: OccasionDto) {
    return this.healthDepartmentService.deleteOccasion(occasion);
  }
}
