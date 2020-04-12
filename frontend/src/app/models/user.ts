import { Client } from './client';
import { HealthDepartmentDto } from './healtDepartment';

export interface User {
  client?: Client;
  healthDepartment: HealthDepartmentDto;
  username: string;
}
