/// <reference types="cypress" />

const newPassword = 'Pa$$w0rd';
const username = 'secAdmin2';

describe('reset account password', () => {
  beforeEach(() => {
    cy.server();
    cy.route('PUT', `/hd/accounts/*/password`).as('resetpassword');

    cy.loginAdmin();

    cy.visit('administration/accounts/account-list');
    cy.get('.ag-center-cols-container > .ag-row').eq(1).click();
    cy.get('.mat-tab-links a:nth-child(2)').click();
  });

  describe('field validations: required', () => {
    describe('enabled submit button', () => {
      it('valid form', () => {
        cy.get('[data-cy="input-password"] input[matInput]').type(newPassword);
        cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type(newPassword);
        cy.get('[data-cy="account-submitandclose-button"] button').should('be.enabled');
      });
    });

    describe('disabled submit button', () => {
      it('empty form', () => {
        cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
        cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
      });

      it('missing password', () => {
        cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type(newPassword);

        cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
        cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
      });

      it('missing password confirm', () => {
        cy.get('[data-cy="input-password"] input[matInput]').type(newPassword);

        cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
        cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
      });
    });
  });

  describe('field tests', () => {
    describe('password', () => {
      it('too few characters', () => {
        cy.get('[data-cy="input-password"] input[matInput]')
          .type('short')
          .blur()
          .then(($input) => {
            $input.hasClass('mat-form-field-invalid');
            cy.get('[data-cy="input-password"] mat-error')
              .should('exist')
              .and('contain.text', 'Dieses Feld erfordert eine Eingabe von mindestens 7 Zeichen')
              .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten')
              .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten')
              .and(
                'contain.text',
                'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
              );
          });
      });

      it('no capital letters', () => {
        cy.get('[data-cy="input-password"] input[matInput]')
          .type('thisispassword1!')
          .blur()
          .then(($input) => {
            $input.hasClass('mat-form-field-invalid');
            cy.get('[data-cy="input-password"] mat-error')
              .should('exist')
              .and('contain.text', 'Dieses Feld muss mindestens einen Großbuchstaben enthalten');
          });
      });

      it('no numbers', () => {
        cy.get('[data-cy="input-password"] input[matInput]')
          .type('thisIsMyPassword!')
          .blur()
          .then(($input) => {
            $input.hasClass('mat-form-field-invalid');
            cy.get('[data-cy="input-password"] mat-error')
              .should('exist')
              .and('contain.text', 'Dieses Feld muss mindestens eine Zahl enthalten');
          });
      });

      it('no special characters', () => {
        cy.get('[data-cy="input-password"] input[matInput]')
          .type('thisIsMyPassword1')
          .blur()
          .then(($input) => {
            $input.hasClass('mat-form-field-invalid');
            cy.get('[data-cy="input-password"] mat-error')
              .should('exist')
              .and(
                'contain.text',
                'Dieses Feld muss mindestens eines der folgenden Sonderzeichen beinhalten: @ # $ % ^ & * ( ) , . ? : | & < >'
              );
          });
      });

      it('password and confirmation have to match', () => {
        cy.get('[data-cy="input-password"] input[matInput]')
          .type('thisIsMyPassword1!')
          .blur()
          .then(($input) => {
            $input.hasClass('ng-valid');
            cy.get('[data-cy="input-password"] mat-error').should('not.exist');
          });

        cy.get('[data-cy="input-passwordconfirm"] input[matInput]')
          .type('thisIsMyPassword12!')
          .blur()
          .then(($input) => {
            $input.hasClass('mat-form-field-invalid');
            cy.get('[data-cy="input-passwordconfirm"] mat-error')
              .should('exist')
              .and('contain.text', 'Das Passwort und die Bestätigung müssen übereinstimmen');
          });
      });
    });
  });

  describe('password reset', () => {
    it('valid form', () => {
      cy.get('[data-cy="input-password"] input[matInput]').type(newPassword);
      cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type(newPassword);
      cy.get('[data-cy="account-submitandclose-button"] button').should('be.enabled');
      cy.get('[data-cy="account-submitandclose-button"] button').click();

      cy.wait('@resetpassword').its('status').should('eq', 204);
      cy.url().should('include', `/administration/accounts/account-list`);
    });
  });
});

describe('login with new password', () => {
  beforeEach(() => {
    cy.server();
    cy.route('PUT', '/user/me/password').as('changepassword');
  });

  it('login with new account should open change password dialog', () => {
    cy.login(username, newPassword);
    cy.get('mat-dialog-container').should('exist');
    cy.get('mat-dialog-container mat-card-title h1').should('exist').should('have.text', 'Passwort ändern');
    cy.get('mat-dialog-container [data-cy="input-username"] input[matInput]').should('contain.value', username);
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.disabled');
    cy.get('mat-dialog-container [data-cy="input-currentpassword"] input[matInput]').type(newPassword);
    cy.get('mat-dialog-container [data-cy="input-newpassword"] input[matInput]').type('New123!');
    cy.get('mat-dialog-container [data-cy="input-password-confirm"] input[matInput]').type('New123!');
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.enabled');
    cy.get('[data-cy="changepassword-submit-button"] button').click();

    cy.wait('@changepassword').its('status').should('eq', 204);
    cy.url().should('include', '/health-department/index-cases/case-list');
  });
});
