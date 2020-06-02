import { ClientDto } from '@qro/client/domain';
import { HealthDepartmentDto } from '@qro/health-department/domain';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}
