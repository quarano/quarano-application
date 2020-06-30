export interface HalResponse {
  _links?: {
    self: { href: string };
    [key: string]: { href: string };
  };
}
