/// <reference types="cypress" />

describe('Account administration', () => {
  beforeEach(() => {
    cy.server();
    cy.route('PUT', `/hd/accounts/*/password`).as('resetPassword');
    cy.route('GET', `/hd/accounts`).as('fetchAccounts');
    cy.route('GET', `/hd/accounts/*`).as('fetchAccount');
    cy.route('PUT', '/user/me/password').as('changePassword');

    cy.logInAdmin();
  });

  it.skip('should reset password and change during next log-in', () => {
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
    cy.get('[data-cy="account-administration"]').click();
    cy.location('pathname').should('eq', '/administration/accounts/account-list');
    cy.wait('@fetchAccounts').its('status').should('eq', 200);
    cy.get('.ag-row').should('have.length.gt', 0);
    cy.get('[row-index="0"] > [aria-colindex="1"]')
      .should('exist')
      .then(($elem) => {
        $elem.click();
      });
    cy.location('pathname').should('include', '/edit');
    cy.wait('@fetchAccount').its('status').should('eq', 200);
    cy.get('@fetchAccount')
      .its('response.body')
      .then((body) => {
        const accountId = body.accountId;
        expect(accountId).not.to.eq(null);
      });

    cy.get('[data-cy="reset-account-password"]').click();
    cy.location('pathname').should('include', '/reset-password');

    const username = 'secAdmin2';
    const newPassword = 'Pa$$w0rd';

    cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
    cy.get('[data-cy="input-password"] input[matInput]').type(newPassword);
    cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type(newPassword);
    cy.get('[data-cy="account-submitandclose-button"] button').should('be.enabled');
    cy.get('[data-cy="account-submitandclose-button"] button').click();

    cy.wait('@resetPassword').its('status').should('eq', 204);
    cy.location('pathname').should('eq', `/administration/accounts/account-list`);
    // TODO: Meldung "erfolgreich aktualisiert"
    cy.logOut();

    cy.logIn(username, newPassword);

    cy.get('mat-dialog-container mat-card-title h1').should('have.text', 'Passwort ändern');
    cy.get('mat-dialog-container [data-cy="input-username"] input[matInput]').should('contain.value', username);
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.disabled');
    cy.get('mat-dialog-container [data-cy="input-currentpassword"] input[matInput]').type(newPassword);
    cy.get('mat-dialog-container [data-cy="input-newpassword"] input[matInput]').type('New123!');
    cy.get('mat-dialog-container [data-cy="input-password-confirm"] input[matInput]').type('New123!');
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.enabled');
    cy.get('[data-cy="changepassword-submit-button"] button').click();

    cy.wait('@changePassword').its('status').should('eq', 204);
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
  });

  // TODO: unit tests
  //
  // it('empty form', () => {
  //   cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
  //   cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
  // });
  //
  // it('missing password', () => {
  //   cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type(newPassword);
  //
  //   cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
  //   cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
  // });
  //
  // it('missing password confirm', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]').type(newPassword);
  //
  //   cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
  //   cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
  // });
  //
  // it('too few characters', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]')
  //     .type('short')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('mat-form-field-invalid');
  //       cy.get('[data-cy="input-password"] mat-error')
  //         .should('exist')
  //         .and('contain.text', 'Dieses Feld erfordert eine Eingabe von mindestens 7 Zeichen')
  //         .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten')
  //         .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten')
  //         .and(
  //           'contain.text',
  //           'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
  //         );
  //     });
  // });
  //
  // it('no capital letters', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]')
  //     .type('thisispassword1!')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('mat-form-field-invalid');
  //       cy.get('[data-cy="input-password"] mat-error')
  //         .should('exist')
  //         .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten');
  //     });
  // });
  //
  // it('no numbers', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]')
  //     .type('thisIsMyPassword!')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('mat-form-field-invalid');
  //       cy.get('[data-cy="input-password"] mat-error')
  //         .should('exist')
  //         .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten');
  //     });
  // });
  //
  // it('no special characters', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]')
  //     .type('thisIsMyPassword1')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('mat-form-field-invalid');
  //       cy.get('[data-cy="input-password"] mat-error')
  //         .should('exist')
  //         .and(
  //           'contain.text',
  //           'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
  //         );
  //     });
  // });
  //
  // it('password and confirmation have to match', () => {
  //   cy.get('[data-cy="input-password"] input[matInput]')
  //     .type('thisIsMyPassword1!')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('ng-valid');
  //       cy.get('[data-cy="input-password"] mat-error').should('not.exist');
  //     });
  //
  //   cy.get('[data-cy="input-passwordconfirm"] input[matInput]')
  //     .type('thisIsMyPassword12!')
  //     .blur()
  //     .then(($input) => {
  //       $input.hasClass('mat-form-field-invalid');
  //       cy.get('[data-cy="input-passwordconfirm"] mat-error')
  //         .should('exist')
  //         .and('contain.text', 'Das Passwort und die Bestätigung müssen übereinstimmen');
  //     });
  // });
});
