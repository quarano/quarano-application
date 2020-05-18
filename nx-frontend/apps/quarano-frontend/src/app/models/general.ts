export interface IIdentifiable {
  id: string;
}

export interface Link {
  href: string;
}

export interface SelfLink {
  self: Link;
}

export interface CreateLink {
  create: Link;
}

export interface EditLink {
  edit: Link;
}

export interface DeleteLink {
  delete: Link;
}
