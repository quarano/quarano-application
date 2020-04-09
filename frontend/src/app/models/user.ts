import {Client} from './client';
import {HealthDepartmentDto} from './healtDepartment';

export interface User {
  client?: Client;
  healthdepartment: HealthDepartmentDto;
  healthdepartmentid: string;
  username: string;
}
