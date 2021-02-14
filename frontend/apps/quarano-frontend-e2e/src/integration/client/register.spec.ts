/// <reference types="cypress" />

xdescribe('registration form', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/registration').as('registration');
    cy.visit('client/enrollment/register');
  });

  // TODO: happy path

  xdescribe('completed form', () => {
    it('returns error if activation code is incorrect', () => {
      cy.get('[data-cy="registration-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="input-client-code"] input[matInput]').type('a557222e-c10c-4c2c-8cdb-41855ba74ac7');
      cy.get('[data-cy="input-username"] input[matInput]').type('my_username');
      cy.get('[data-cy="input-password"] input[matInput]').type('thisIsMyPassword1!');
      cy.get('[data-cy="input-password-confirm"] input[matInput]').type('thisIsMyPassword1!');
      cy.get('[data-cy="input-dateofbirth"] input[matInput]').type('24.05.1965');
      cy.get('[data-cy="input-privacy-policy"]').click();
      cy.get('[data-cy="registration-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="registration-submit-button"] button').click();
      cy.wait('@registration').its('status').should('eq', 400);
      cy.location('pathname').should('eq', '/client/enrollment/register');
    });
  });
  // TODO: unit tests
  // xdescribe('validation', () => {
  //   xdescribe('empty fields', () => {
  //     ['client-code', 'username', 'password', 'password-confirm', 'dateofbirth'].forEach((fieldName: string) => {
  //       it(`empty ${fieldName}`, () => {
  //         cy.get(`[data-cy="input-${fieldName}"] input[matInput]`)
  //           .focus()
  //           .blur()
  //           .then(($input) => $input.hasClass('mat-form-field-invalid'));
  //       });
  //     });
  //   });
  //
  //   xdescribe('input values are validated', () => {
  //     xdescribe('username', () => {
  //       it('valid value', () => {
  //         cy.get('[data-cy="input-username"] input[matInput]')
  //           .type('my_username')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('ng-valid');
  //             cy.get('[data-cy="input-username"] mat-error').should('not.exist');
  //           });
  //       });
  //     });
  //
  //     xdescribe('password', () => {
  //       it('too few characters', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('short')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password"] mat-error')
  //               .should('exist')
  //               .and('contain.text', 'Dieses Feld erfordert eine Eingabe von mindestens 7 Zeichen')
  //               .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten')
  //               .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten')
  //               .and(
  //                 'contain.text',
  //                 'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
  //               );
  //           });
  //       });
  //
  //       it('too many characters', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('thisismyloooooooooooooooooooooooooooooongpassword')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password"] mat-error')
  //               .should('exist')
  //               // limitation does not seem to exist any more
  //               // .and('contain.text', 'Das Passwort darf höchstens 30 Zeichen lang sein.')
  //               .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten')
  //               .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten')
  //               .and(
  //                 'contain.text',
  //                 'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
  //               );
  //           });
  //       });
  //
  //       it('no capital letters', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('thisispassword1!')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password"] mat-error')
  //               .should('exist')
  //               .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten');
  //           });
  //       });
  //
  //       it('no numbers', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('thisIsMyPassword!')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password"] mat-error')
  //               .should('exist')
  //               .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten');
  //           });
  //       });
  //
  //       it('no special characters', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('thisIsMyPassword1')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password"] mat-error')
  //               .should('exist')
  //               .and(
  //                 'contain.text',
  //                 'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
  //               );
  //           });
  //       });
  //
  //       it('password and confirmation have to match', () => {
  //         cy.get('[data-cy="input-password"] input[matInput]')
  //           .type('thisIsMyPassword1!')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('ng-valid');
  //             cy.get('[data-cy="input-password"] mat-error').should('not.exist');
  //           });
  //
  //         cy.get('[data-cy="input-password-confirm"] input[matInput]')
  //           .type('thisIsMyPassword12!')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-password-confirm"] mat-error')
  //               .should('exist')
  //               .and('contain.text', 'Das Passwort und die Bestätigung müssen übereinstimmen');
  //           });
  //       });
  //     });
  //
  //     xdescribe('date of birth', () => {
  //       it('no date string', () => {
  //         cy.get('[data-cy="input-dateofbirth"] input[matInput]')
  //           .type('somestring')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('mat-form-field-invalid');
  //             cy.get('[data-cy="input-dateofbirth"] mat-error')
  //               .should('exist')
  //               .and('contain.text', 'Bitte füllen Sie dieses Pflichtfeld aus');
  //           });
  //       });
  //
  //       it('valid date string', () => {
  //         cy.get('[data-cy="input-dateofbirth"] input[matInput]')
  //           .type('24.05.1965')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('ng-valid');
  //             cy.get('[data-cy="input-dateofbirth"] mat-error').should('not.exist');
  //           });
  //       });
  //
  //       it('date shortcuts are valid', () => {
  //         cy.get('[data-cy="input-dateofbirth"] input[matInput]')
  //           .type('12031964')
  //           .blur()
  //           .then(($input) => {
  //             $input.hasClass('ng-valid');
  //             cy.get('[data-cy="input-dateofbirth"] mat-error').should('not.exist');
  //             cy.get('[data-cy="input-dateofbirth"] input[matInput]').should('contain.value', '12.3.1964');
  //           });
  //       });
  //     });
  //   });
  // });
});
