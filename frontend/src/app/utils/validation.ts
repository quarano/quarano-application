export const VALIDATION_PATTERNS = {
  integerUnsigned: '^[0-9]+$',

  alphanumeric: '[\\p{L}\\s\\d]*',
  alphabetic: '[\\p{L}\\s]*',
  textual: '[\\p{L}\\s\\d\\.\\,\\?\\!\\(\\)\\-\\n\\r]*',

  uuid: '\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b',

  zip: '[0-9]{5}',
  street: '[\\p{L}\\s\\.\\-[0-9]]*',
  houseNumber: '[\\p{L}\\s\\.\\-[0-9]*\\/]{0,15}',
  
  name: '[\\p{L}\\s\\-]*',
  username: '[\\p{L}0-9\\-\\_]*',

  phoneNumber: '^[\\+\\(\\)\\s\\-0-9]*?$',
  // tslint:disable-next-line:max-line-length quotemark
  email: "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
};
