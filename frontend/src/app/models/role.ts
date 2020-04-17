export interface IRole {
  name: string;
  isHealthDepartmentUser: boolean;
}

export const roleNames = {
  user: 'ROLE_USER',
  healthDepartmentAdmin: 'ROLE_HD_ADMIN',
  healthDepartmentCaseAgent: 'ROLE_HD_CASE_AGENT'
};

export const roles: IRole[] = [
  { name: roleNames.user, isHealthDepartmentUser: false },
  { name: roleNames.healthDepartmentAdmin, isHealthDepartmentUser: true },
  { name: roleNames.healthDepartmentCaseAgent, isHealthDepartmentUser: true }
];
