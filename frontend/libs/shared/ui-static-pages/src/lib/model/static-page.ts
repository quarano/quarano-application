export interface StaticPageDto {
  key: StaticPageKeys;
  text: string;
}

export enum StaticPageKeys {
  Terms = 'terms',
  DataProtection = 'data-protection',
  Imprint = 'imprint',
  WelcomeIndex = 'welcome-index',
  WelcomeContact = 'welcome-contact',
}
