import {ClientDto} from './client';
import {HealthDepartmentDto} from './healthDepartment';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}
