import { EntityMetadataMap } from '@ngrx/data';

const entityMetadata: EntityMetadataMap = {
  Case: {},
};

// @todo SC: CORE-341 wenn wir keine Plurals brauchen weg damit.
const pluralNames = { Case: 'CaseDetails' };

export const healthDepartmentConfig = {
  entityMetadata,
  pluralNames,
};
