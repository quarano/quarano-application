import { FormControl, AbstractControl } from '@angular/forms';
export class ValidationErrorGenerator {

  public static getErrorKeys(control: AbstractControl): string[] {
    if (!control.errors) { return null; }
    return Object.keys(control.errors);
  }

  public static getErrorMessage(control: AbstractControl, errorKey: string): string {
    if (control.valid) { return ''; }
    switch (errorKey) {
      case 'required': return 'Bitte füllen Sie dieses Pflichtfeld aus';
      case 'error400': return control.getError('error400').errorMessage;
      case 'trimmedPattern': return control.getError('trimmedPattern').errorMessage;
      case 'minlength': return `Dieses Feld erfordert eine Eingabe von mindestens ${control.getError('minlength').requiredLength} Zeichen`;
      case 'maxlength': return `Dieses Feld darf maximal ${control.getError('maxlength').requiredLength} Zeichen enthalten`;
      case 'minLengthArray': return 'Bitte wählen Sie mindestens ein Element';
      case 'uppercase': return 'Dieses Feld muss mindestens einen Großbuchstaben enthalten';
      case 'digit': return 'Dieses Feld muss mindestens eine Zahl enthalten';
      case 'nonWordChar': return 'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >';
      case 'passwordIncludesUsername': return 'Das Passwort darf nicht den Benutzernamen beinhalten';
      case 'passwordConfirmWrong': return 'Das Passwort und die Bestätigung müssen übereinstimmen';
      case 'usernameInvalid': return 'Der angegebene Benutzername kann nicht verwendet werden';
      case 'codeInvalid': return 'Der eingegebene Code ist ungültig';
    }
  }
}
