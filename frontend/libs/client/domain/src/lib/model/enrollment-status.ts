import { HalResponse } from '@qro/shared/util-data-access';

export interface EnrollmentStatusDto extends HalResponse {
  completedPersonalData: boolean;
  completedQuestionnaire: boolean;
  completedContactRetro: boolean;
  complete: boolean;
  steps: string[];
}

export function getEmptyEnrollmentStatus(): EnrollmentStatusDto {
  return {
    completedPersonalData: false,
    completedQuestionnaire: false,
    completedContactRetro: false,
    complete: false,
    steps: [],
  };
}
