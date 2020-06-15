const characterSet = 'a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒß';

export interface IValidationPattern {
  pattern: string | RegExp;
  errorMessage: string;
}

export enum VALIDATION_PATTERNS {
  integerUnsigned = 'integerUnsigned',
  alphanumeric = 'alphanumeric',
  alphabetic = 'alphabetic',
  textual = 'textual',
  uuid = 'uuid',
  zip = 'zip',
  street = 'street',
  houseNumber = 'houseNumber',
  name = 'name',
  username = 'username',
  phoneNumber = 'phoneNumber',
  email = 'email',
  city = 'city',
  extReferenceNumber = 'extReferenceNumber',
}

export class ValidationPattern {
  private static _patterns: Map<VALIDATION_PATTERNS, IValidationPattern>;

  public static get validationPatterns() {
    if (!this._patterns) {
      this.initialize();
    }
    return this._patterns;
  }

  private static initialize() {
    this._patterns = new Map<VALIDATION_PATTERNS, IValidationPattern>();
    this._patterns.set(VALIDATION_PATTERNS.integerUnsigned, {
      pattern: '^[0-9]+$',
      errorMessage: 'Dieses Feld darf nur Ziffern enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.alphanumeric, {
      pattern: `[${characterSet}\\s\\d]*`,
      errorMessage: 'Dieses Feld darf nur Buchstaben und Zahlen enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.alphabetic, {
      pattern: `[${characterSet}\\s]*`,
      errorMessage: 'Dieses Feld darf nur Buchstaben enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.textual, {
      pattern: `[${characterSet}\\s\\d\\.\\,\\?\\!\\(\\)\\-\\n\\r]*`,
      errorMessage: 'Dieses Feld darf nur Zahlen, Buchstaben, Satzzeichen und Zeilenumbrüche enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.uuid, {
      pattern: '\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b',
      errorMessage: 'Bitte geben Sie eine gültige UUID ein',
    });
    this._patterns.set(VALIDATION_PATTERNS.zip, {
      pattern: '[0-9]{5}',
      errorMessage: 'Bitte geben Sie eine gültige Postleitzahl aus 5 Ziffern an',
    });
    this._patterns.set(VALIDATION_PATTERNS.street, {
      pattern: `[${characterSet}0-9\\s\\.\\-]*`,
      errorMessage:
        'Bitte geben Sie eine gültige Straße an. Diese darf Buchstaben, Zahlen, Leerzeichen, ' +
        'Punkte und Bindestriche enthalten.',
    });
    this._patterns.set(VALIDATION_PATTERNS.houseNumber, {
      pattern: `[${characterSet}0-9\\s\\.\\-*\\/]{0,15}`,
      errorMessage:
        'Bitte geben Sie eine gültige Hausnummer an. Diese darf maximal 15 Zeichen lang sein und ' +
        'nur aus Buchstaben, Zahlen, Punkten, Binde- und Schrägstrichen bestehen',
    });
    this._patterns.set(VALIDATION_PATTERNS.name, {
      pattern: `([${characterSet}\\s\\-]*)`,
      errorMessage:
        'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt',
    });
    this._patterns.set(VALIDATION_PATTERNS.username, {
      pattern: `([${characterSet}0-9\\-\\_]*)`,
      errorMessage:
        'Bitte geben Sie einen gültigen Benutzernamen ein! Dieser kann Buchstaben, Zahlen, Binde- oder Unterstriche enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.phoneNumber, {
      pattern: '^[\\+\\(\\)\\s\\-0-9]*?$',
      errorMessage:
        'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten',
    });
    this._patterns.set(VALIDATION_PATTERNS.email, {
      // tslint:disable-next-line: max-line-length quotemark
      pattern:
        '(?:[a-zA-Z0-9!#$%&\' * +/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])',
      errorMessage: 'Bitte geben Sie eine gültige E-Mail-Adresse an',
    });
    this._patterns.set(VALIDATION_PATTERNS.city, {
      pattern: `[${characterSet}\\s\\.\\-\\(\\)\\/]*`,
      errorMessage:
        'Bitte geben Sie eine gültige Stadt an" Diese kann Buchstaben und folgende Zeichen enthalten: . - ( ) /',
    });
    this._patterns.set(VALIDATION_PATTERNS.extReferenceNumber, {
      pattern: `[${characterSet}0-9\\-\\_\\/]*`,
      errorMessage:
        'Die eingegebene Vorgangsnummer enthält nicht erlaubte Zeichen. Vorgangsnummern dürfen nur aus Zahlen, Buchstaben, Binde- und Unterstrichen bestehen',
    });
  }
}
