import { ClientDto } from '../../../../../libs/client/domain/src/lib/models/client';
import { HealthDepartmentDto } from './healthDepartment';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}
