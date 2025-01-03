export interface Role {
  roleId: number;
  name: string;
}

export interface User {
  id: number;
  mail: string;
  lastName: string;
  firstName: string;
  country: string;
  city: string;
  street: string;
  postalCode: string;
  active: boolean;
  blocked: boolean;
  role: Role;
}
