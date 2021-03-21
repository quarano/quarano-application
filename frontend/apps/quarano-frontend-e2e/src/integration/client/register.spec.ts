/// <reference types="cypress" />

describe('registration form', () => {
  beforeEach(() => {
    cy.intercept('GET', '/user/me').as('me');
    cy.intercept({
      method: 'PUT',
      path: /^\/hd\/cases\/\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b\/registration$/,
    }).as('newActivationCode');
    cy.intercept('POST', '/registration').as('registration');
    cy.intercept({
      method: 'GET',
      path: /^\/hd\/cases\/$/,
    }).as('allCases');
    cy.intercept({
      method: 'GET',
      path: /^\/hd\/cases\/\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b$/,
    }).as('specificCase');
  });

  function extractActivationCode(elem: JQuery) {
    const regex = /\/client\/enrollment\/landing\/index\/(.*)/g;
    let content;

    if (typeof elem !== 'string') {
      content = elem.text();
    } else {
      content = elem;
    }

    try {
      return regex.exec(content)[1];
    } catch (e) {
      cy.log(e);
      throw e;
    }
  }

  describe('completed form', () => {
    it('G13.2-1 renew activation code', () => {
      cy.logInAgent();
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.wait('@me').its('response.statusCode').should('eq', 200);
      cy.wait('@allCases').its('response.statusCode').should('eq', 200);

      cy.get('[data-cy="search-index-case-input"]').type('Aalen');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length', 1)
        .eq(0)
        .click();

      cy.wait('@specificCase').its('response.statusCode').should('eq', 200);
      cy.get('@specificCase')
        .its('response.body')
        .then(($body) => {
          const caseId = $body.caseId;
          cy.location('pathname').should(
            'include',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/edit'
          );
        });

      cy.get('[data-cy="start-tracking-button"]').click();
      cy.wait('@newActivationCode').its('response.statusCode').should('eq', 200);

      cy.get('[data-cy="email-tab"]').click();
      let oldExtractedActivationCode = '';

      cy.get('[data-cy="copy-to-clipboard"]').click();

      cy.get('[data-cy="mail-text"]').then((elem) => {
        oldExtractedActivationCode = extractActivationCode(elem);
      });

      cy.get('[data-cy="new-activation-code"]').click();

      cy.wait('@newActivationCode').its('response.statusCode').should('eq', 200);

      cy.get('[data-cy="new-activation-code"]').should('be.enabled');

      cy.get('[data-cy="copy-to-clipboard"]').click();

      let newExtractedActivationCode = '';

      cy.get('[data-cy="mail-text"]').then((elem) => {
        newExtractedActivationCode = extractActivationCode(elem);
        expect(oldExtractedActivationCode).to.not.eq(newExtractedActivationCode);

        cy.logOut();
        cy.visit('/client/enrollment/landing/index/' + oldExtractedActivationCode);

        cy.get('[data-cy="cta-button-index"]').click();
        cy.get('[data-cy="registration-submit-button"] button').should('be.disabled');

        cy.get('[data-cy="input-username"] input[matInput]').type('Peter');
        cy.get('[data-cy="input-password"] input[matInput]').type('Test1234!');
        cy.get('[data-cy="input-password-confirm"] input[matInput]').type('Test1234!');
        cy.get('[data-cy="input-dateofbirth"] input[matInput]').type('01.01.1990');
        cy.get('[data-cy="input-privacy-policy"]').click();
        cy.get('[data-cy="registration-submit-button"] button').should('be.enabled');
        cy.get('[data-cy="registration-submit-button"] button').click();
        cy.wait('@registration').its('response.statusCode').should('eq', 400);
        cy.location('pathname').should('include', '/client/enrollment/register');
        cy.get('mat-error').contains(
          ' Der von Ihnen eingegebene Anmeldecode ist entweder nicht aktuell oder bereits benutzt worden. Bitte überprüfen Sie, ob Sie einen neueren Code vom Gesundheitsamt erhalten haben. '
        );

        cy.visit('/client/enrollment/landing/index/' + newExtractedActivationCode);

        cy.get('[data-cy="cta-button-index"]').click();
        cy.get('[data-cy="registration-submit-button"] button').should('be.disabled');

        cy.get('[data-cy="input-username"] input[matInput]').type('Peter');
        cy.get('[data-cy="input-password"] input[matInput]').type('Test1234!');
        cy.get('[data-cy="input-password-confirm"] input[matInput]').type('Test1234!');
        cy.get('[data-cy="input-dateofbirth"] input[matInput]').type('01.01.1990');
        cy.get('[data-cy="input-privacy-policy"]').click();
        cy.get('[data-cy="registration-submit-button"] button').should('be.enabled');
        cy.get('[data-cy="registration-submit-button"] button').click();
        cy.wait('@registration').its('response.statusCode').should('eq', 200);

        cy.location('pathname').should('eq', '/client/enrollment/basic-data');
      });
    });
  });

  // TODO: unit tests
  // describe('validation', () => {
  //   describe('empty fields', () => {
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
  //   describe('input values are validated', () => {
  //     describe('username', () => {
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
  //     describe('date of birth', () => {
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
