/// <reference types="cypress" />

/// <reference types="cypress" />

describe('Account administration', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/accounts').as('createAccount');
    cy.route('PUT', '/user/me/password').as('changePassword');
    cy.route('GET', `/hd/accounts`).as('fetchAccounts');
    cy.route('GET', `/user/me`).as('me');
    cy.route('GET', `/hd/cases`).as('allCases');

    cy.loginAdmin();
  });

  it('create new admin account', () => {
    cy.wait('@me').its('status').should('eq', 200);
    cy.wait('@allCases').its('status').should('eq', 200);

    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.get('[data-cy="account-administration"]').click();

    cy.location('pathname').should('eq', '/administration/accounts/account-list');

    cy.wait('@fetchAccounts').its('status').should('eq', 200);

    cy.get('[data-cy="new-case-button"]').click();

    cy.location('pathname').should('eq', '/administration/accounts/account-detail/new/edit');

    cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
    cy.get('[data-cy="account-submit-button"] button').should('be.disabled');

    cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
    cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
    cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
    cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
    cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
    cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
    cy.get('[id="mat-option-0"]').then(($elem) => {
      $elem.trigger('click');
      cy.get('.cdk-overlay-backdrop').click();
      cy.get('[data-cy="account-submitandclose-button"] button').should('be.enabled');
      cy.get('[data-cy="account-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="account-submitandclose-button"] button').click();
    });

    cy.wait('@createAccount').its('status').should('eq', 201);
    cy.location('pathname').should('eq', '/administration/accounts/account-list');
    cy.logOut();
    cy.login('testaccount', 'Test123!');
    cy.get('mat-dialog-container mat-card-title h1').should('have.text', 'Passwort ändern');
    cy.get('mat-dialog-container [data-cy="input-username"] input[matInput]').should('contain.value', 'testaccount');
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.disabled');
    cy.get('mat-dialog-container [data-cy="input-currentpassword"] input[matInput]').type('Test123!');
    cy.get('mat-dialog-container [data-cy="input-newpassword"] input[matInput]').type('New123!');
    cy.get('mat-dialog-container [data-cy="input-password-confirm"] input[matInput]').type('New123!');
    cy.get('[data-cy="changepassword-submit-button"] button').should('be.enabled');
    cy.get('[data-cy="changepassword-submit-button"] button').click();

    cy.wait('@changePassword').its('status').should('eq', 204);
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
    cy.get('[data-cy="profile-user-button"] .mat-button-wrapper span').should(
      'have.text',
      'Testfirstname Testlastname (GA Mannheim) ' // TODO: space
    );
  });
});

//
// describe('new account', () => {
//   beforeEach(() => {
//     cy.server();
//     cy.route('POST', '/hd/accounts').as('createaccount');
//
//     cy.loginAdmin();
//
//     cy.visit('administration/accounts/account-detail/new');
//   });
//
//   describe('field validations: required', () => {
//     describe('enabled submit button', () => {

// TODO: unit tests

//       it('missing firstname', () => {
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing lastname', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing e-mail', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing username', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing password', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing password confirm', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//
//       it('missing role', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-username"] input[matInput]').type('testaccount');
//         cy.get('[data-cy="input-password"] input[matInput]').type('Test123!');
//
//         cy.get('[data-cy="account-submit-button"] button').should('be.disabled');
//         cy.get('[data-cy="account-submitandclose-button"] button').should('be.disabled');
//       });
//     });
//   });
//
//   describe('field tests', () => {
//     describe('firstName', () => {
//       it('should not be empty', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
//         cy.get('[data-cy="input-firstname"] input[matInput]').clear();
//         cy.get('[data-cy="input-firstname"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-firstname"] mat-error').should(
//           'contain.text',
//           'Bitte füllen Sie dieses Pflichtfeld aus'
//         );
//       });
//
//       it('should be invalid', () => {
//         cy.get('[data-cy="input-firstname"] input[matInput]').clear().type('Testfirstn5ame');
//         cy.get('[data-cy="input-firstname"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-firstname"] mat-error').should(
//           'contain.text',
//           'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
//         );
//       });
//     });
//
//     describe('lastName', () => {
//       it('should not be empty', () => {
//         cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
//         cy.get('[data-cy="input-lastname"] input[matInput]').clear();
//         cy.get('[data-cy="input-lastname"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-lastname"] mat-error').should(
//           'contain.text',
//           'Bitte füllen Sie dieses Pflichtfeld aus'
//         );
//       });
//       it('should be invalid', () => {
//         cy.get('[data-cy="input-lastname"] input[matInput]').clear().type('Testlast09name');
//         cy.get('[data-cy="input-lastname"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-lastname"] mat-error').should(
//           'contain.text',
//           'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
//         );
//       });
//     });
//
//     describe('username', () => {
//       it('should not be empty', () => {
//         cy.get('[data-cy="input-username"] input[matInput]').type('account');
//         cy.get('[data-cy="input-username"] input[matInput]').clear();
//         cy.get('[data-cy="input-username"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-username"] mat-error').should(
//           'contain.text',
//           'Bitte füllen Sie dieses Pflichtfeld aus'
//         );
//       });
//
//       it('should be invalid', () => {
//         cy.get('[data-cy="input-username"] input[matInput]').clear().type('admin');
//         cy.get('[data-cy="input-username"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-username"] mat-error').should(
//           'contain.text',
//           'Der angegebene Benutzername kann nicht verwendet werden '
//         );
//       });
//     });
//
//     describe('password', () => {
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
//         cy.get('[data-cy="input-passwordconfirm"] input[matInput]')
//           .type('thisIsMyPassword12!')
//           .blur()
//           .then(($input) => {
//             $input.hasClass('mat-form-field-invalid');
//             cy.get('[data-cy="input-passwordconfirm"] mat-error')
//               .should('exist')
//               .and('contain.text', 'Das Passwort und die Bestätigung müssen übereinstimmen');
//           });
//       });
//     });
//
//     describe('role', () => {
//       it('should not be empty', () => {
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//         cy.get('.cdk-overlay-backdrop').click();
//         cy.get('[data-cy="select-role"] div.mat-select-trigger').click();
//         cy.get('[id="mat-option-0"]').click();
//
//         cy.get('[data-cy="select-role"] mat-error').should('contain.text', 'Bitte wählen Sie mindestens ein Element ');
//       });
//     });
//
//     describe('email address', () => {
//       it('should have a valid email address', () => {
//         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
//         cy.get('[data-cy="input-email"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-email"] mat-error').should('not.exist');
//       });
//
//       it('should be invalid without correct email structure', () => {
//         cy.get('[data-cy="input-email"] input[matInput]').type('te@st@test.de');
//         cy.get('[data-cy="input-email"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-email"] mat-error').should('exist');
//
//         cy.get('[data-cy="input-email"] input[matInput]').clear().type('test.de');
//         cy.get('[data-cy="input-email"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-email"] mat-error').should('exist');
//
//         cy.get('[data-cy="input-email"] input[matInput]').clear().type('test@testde');
//         cy.get('[data-cy="input-email"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-email"] mat-error').should('exist');
//
//         cy.get('[data-cy="input-email"] input[matInput]').clear().type('testde');
//         cy.get('[data-cy="input-email"] input[matInput]').blur();
//
//         cy.get('[data-cy="input-email"] mat-error').should('exist');
//       });
//     });
//   });
//
//     });
//   });
// });
//
//
// });
