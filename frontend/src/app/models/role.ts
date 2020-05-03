export interface IRole {
  name: string;
  isHealthDepartmentUser: boolean;
  isAdmin: boolean;
}

export const roleNames = {
  user: 'ROLE_USER',
  healthDepartmentAdmin: 'ROLE_HD_ADMIN',
  healthDepartmentCaseAgent: 'ROLE_HD_CASE_AGENT',
  quaranoAdmin: 'ROLE_QUARANO_ADMIN'
};

export const roles: IRole[] = [
  { name: roleNames.user, isHealthDepartmentUser: false, isAdmin: false },
  { name: roleNames.healthDepartmentAdmin, isHealthDepartmentUser: true, isAdmin: true },
  { name: roleNames.healthDepartmentCaseAgent, isHealthDepartmentUser: true, isAdmin: false },
  { name: roleNames.quaranoAdmin, isHealthDepartmentUser: false, isAdmin: true },
];
