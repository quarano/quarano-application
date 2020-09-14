import { map } from 'rxjs/operators';
import { CaseDto } from '@qro/health-department/domain';
import { Dictionary } from '@ngrx/entity';
import { Store } from '@ngrx/store';

export const mapCaseIdToCaseEntity = () =>
  map(
    (target: [string, Dictionary<CaseDto> | Store<Dictionary<CaseDto>>]): CaseDto => {
      const id = target[0];
      const entityMap = target[1];
      if (!id) {
        return {
          firstName: null,
          lastName: null,
          quarantineEndDate: null,
          quarantineStartDate: null,
          dateOfBirth: null,
          infected: null,
          caseTypeLabel: null,
          extReferenceNumber: null,
          contactCount: null,
          originCases: [],
          _embedded: { originCases: [] },
        } as CaseDto;
      } else {
        return entityMap[id];
      }
    }
  );
