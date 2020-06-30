import { ClientDto } from './client';
import { HealthDepartmentDto } from './health-department';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}
