import { Link } from './general';
import { Alert } from '@quarano-frontend/health-department/domain';

export interface CaseActionDto {
  caseId: string;
  anomalies: AnomaliesDto;
  _links: ResolveLink;
}

export interface ResolveLink {
  resolve: Link;
}

export interface AnomaliesDto {
  process: ActionDto[];
  resolved: ActionDto[];
  health: ActionDto[];
}

export interface ActionDto {
  date: string;
  items: AnomalyDto[];
}

export interface AnomalyDto {
  type: Alert;
  date: Date;
  description: string;
  weight: number;
  _links: EntryLink;
}

export interface EntryLink {
  entry: Link;
}
