/// <reference types="cypress" />

describe('new case', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/api/hd/cases'/*, 'fixture:get-api-hd-cases.json'*/).as('createcase');
    cy.route('PUT', '/api/hd/cases'/*, 'fixture:get-api-hd-cases.json'*/).as('updatecase');

    cy.loginAgent();

    cy.visit('http://localhost:4200/tenant-admin/client');
  });
  /*
    describe('field validations: required', () => {
      describe('enabled submit button', () => {
        it('valid form (with phone)', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
          cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

          cy.get('[data-cy="client-submit-button"]').should('be.enabled');
        });

        it('valid form (with mobile)', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
          cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 239 120 1');

          cy.get('[data-cy="client-submit-button"]').should('be.enabled');
        });
      });

      describe('disabled submit button', () => {
        it('empty form', () => {
          cy.get('[data-cy="client-submit-button"]').should('be.disabled');
        });

        it('missing firstname', () => {
          cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');

          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
          cy.get('[data-cy="client-submit-button"]').should('be.disabled');
        });

        it('missing lastname', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');

          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
          cy.get('[data-cy="client-submit-button"]').should('be.disabled');
        });

        it('missing phone or mobile', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');
          cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');

          cy.get('[data-cy="client-submit-button"]').should('be.disabled');
        });
      });
    });
  */
  describe('field validations', () => {
    /*describe('firstName', () => {
      it('should not be empty', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-firstname"] input[matInput]').clear();
        cy.get('[data-cy="input-firstname"] input[matInput]').blur();

        cy.get('[data-cy="input-firstname"] mat-error').should('contain.text', 'Bitte geben Sie einen Vornamen ein');
      });
    });

    describe('lastName', () => {
      it('should not be empty', () => {
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-lastname"] input[matInput]').clear();
        cy.get('[data-cy="input-lastname"] input[matInput]').blur();

        cy.get('[data-cy="input-lastname"] mat-error').should('contain.text', 'Bitte geben Sie einen Nachnamen ein');
      });
    });

    describe('dayOfBirth', () => {
      it('should be a date string', () => {
        cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('invalid');
        cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

        cy.get('[data-cy="input-dayofbirth"] mat-error').should('contain.text', 'Bitte geben Sie ein gültiges Datum ein');
      });

      it('should be a date string lower or equal today', () => {
        const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString();

        cy.get('[data-cy="input-dayofbirth"] input[matInput]').type(dateTomorrow);
        cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

        cy.get('[data-cy="input-dayofbirth"] mat-error').should('contain.text', 'Bitte geben Sie ein gültiges Datum ein');
      });

      it('should be valid a correct date string', () => {
        cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('20.12.1987');
        cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

        cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
      });

      it('should open the date picker and click valid date', () => {
        cy.get('[data-cy="input-dayofbirth"] mat-datepicker-toggle').click();
        cy.get('mat-datepicker-content').should('exist');
        cy.get('mat-datepicker-content .mat-calendar-body-today').click();
        cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
      });
    });*/
  });
});
