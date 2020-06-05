import { ClientDto } from '@qro/client/api';
import { HealthDepartmentDto } from '@qro/health-department/api';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}
