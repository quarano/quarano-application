export interface IRole {
  name: string;
  isHealthDepartmentUser: boolean;
  isAdmin: boolean;
  displayName: string;
}

export const roleNames = {
  user: 'ROLE_USER',
  healthDepartmentAdmin: 'ROLE_HD_ADMIN',
  healthDepartmentCaseAgent: 'ROLE_HD_CASE_AGENT',
  quaranoAdmin: 'ROLE_QUARANO_ADMIN'
};

export const roles: IRole[] = [
  { name: roleNames.user, isHealthDepartmentUser: false, isAdmin: false, displayName: 'Bürger' },
  { name: roleNames.healthDepartmentAdmin, isHealthDepartmentUser: true, isAdmin: true, displayName: 'Gesundheitsamtsadmin' },
  { name: roleNames.healthDepartmentCaseAgent, isHealthDepartmentUser: true, isAdmin: false, displayName: 'Fallbearbeiter' },
  { name: roleNames.quaranoAdmin, isHealthDepartmentUser: false, isAdmin: true, displayName: 'Quarano-Admin' },
];
