import { IIdentifiable, DeleteLink } from './general';
import { ClientDto } from './client';
import { HealthDepartmentDto } from './healthDepartment';

export interface UserDto {
  client?: ClientDto;
  healthDepartment: HealthDepartmentDto;
  username: string;
  firstName: string;
  lastName: string;
}

export interface UserListItemDto extends IIdentifiable {
  lastName: string;
  firstName: string;
  username: string;
  roles: string[];
  _links: DeleteLink;
}
