/// <reference types="cypress" />

describe('health-department contact cases', () => {
  beforeEach(() => {
    cy.loginAgent();
    cy.route('POST', '/hd/cases/?type=contact').as('newContact');
    cy.route('PUT', '/hd/cases/*').as('updateIndexCase');
  });

  describe('converting to index case ', () => {
    it('should not be possible if required fields are missing', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
      cy.get('[data-cy="contact-cases"]').should('exist');
      cy.get('[data-cy="contact-cases"]').click();
      cy.location('pathname').should('include', 'health-department/contact-cases/case-list');
      cy.get('[data-cy="case-data-table"]').should('exist');
      cy.get('[data-cy="case-data-table"] .datatable-row-group').eq(1).should('exist');
      cy.get('[data-cy="case-data-table"] .datatable-row-group').eq(1).click();

      cy.location('pathname').should('include', 'health-department/case-detail/contact');

      cy.get('[data-cy="input-field-test-date"]').should('exist');
      cy.get('[data-cy="input-field-test-date"]').type('5.6.2020').blur();
      cy.get('[data-cy="covid-test-result-true"]').should('exist');
      cy.get('[data-cy="covid-test-result-true"]').click();

      cy.get('[data-cy="confirm-button"]').should('exist');
      cy.get('[data-cy="confirm-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');
      cy.get('simple-snack-bar').should('exist');
    });

    it('happy path', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
      cy.get('[data-cy="contact-cases"]').should('exist');
      cy.get('[data-cy="contact-cases"]').click();
      cy.location('pathname').should('include', 'health-department/contact-cases/case-list');
      cy.get('[data-cy="case-data-table"]').should('exist');
      cy.get('[data-cy="case-data-table"] .datatable-row-group').eq(1).should('exist');
      cy.get('[data-cy="case-data-table"] .datatable-row-group').eq(1).click();

      cy.location('pathname').should('include', 'health-department/case-detail/contact');

      cy.get('[data-cy="input-field-test-date"]').should('exist');
      cy.get('[data-cy="input-field-test-date"]').type('5.6.2020').blur();
      cy.get('[data-cy="covid-test-result-true"]').should('exist');
      cy.get('[data-cy="covid-test-result-true"]').click();

      cy.get('[data-cy="confirm-button"]').should('exist');
      cy.get('[data-cy="confirm-button"]').click();

      cy.get('[data-cy="quarantine-start-input"]').should('exist');
      cy.get('[data-cy="quarantine-end-input"]').should('exist');
      cy.get('[data-cy="phone-number-input"]').should('exist');

      cy.get('simple-snack-bar button').click();

      cy.get('[data-cy="quarantine-start-input"]').type('6.6.2020').blur();
      cy.get('[data-cy="quarantine-end-input"]').type('20.6.2020').blur();
      cy.get('[data-cy="phone-number-input"]').type('0721123456').blur();

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="zip-code-input"]').should('exist');
      cy.get('[data-cy="street-input"]').should('exist');
      cy.get('[data-cy="house-number-input"]').should('exist');
      cy.get('[data-cy="city-input"]').should('exist');

      cy.get('[data-cy="zip-code-input"]').type('10000');
      cy.get('[data-cy="street-input"]').type('Test');
      cy.get('[data-cy="house-number-input"]').type('1');
      cy.get('[data-cy="city-input"]').type('Berlin');

      cy.get('[data-cy="client-submit-button"] button').click();
      cy.wait('@updateIndexCase').its('status').should('eq', 200);
      cy.get('@updateIndexCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');

          cy.location('pathname').should('include', 'health-department/case-detail/index/' + caseId + '/edit');
        });
    });
  });

  describe('create new contact case', () => {
    it('happy path save and close', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
      cy.get('[data-cy="contact-cases"]').should('exist');
      cy.get('[data-cy="contact-cases"]').click();
      cy.location('pathname').should('include', 'health-department/contact-cases/case-list');
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').should('exist');
      cy.get('[data-cy="input-lastname"]').should('exist');
      cy.get('[data-cy="input-dayofbirth"]').should('exist');
      cy.get('[data-cy="input-phone"]').should('exist');
      cy.get('[data-cy="input-email"]').should('exist');

      cy.get('[data-cy="input-firstname"]').type('Jack');
      cy.get('[data-cy="input-lastname"]').type('Randel');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('jack@gmail.com');

      cy.get('[data-cy="client-submit-and-close-button"] button').click();
      cy.wait('@newContact').its('status').should('eq', 201);
      cy.get('@newContact')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
        });
      cy.location('pathname').should('include', 'health-department/contact-cases/case-list');
    });

    it('happy path save and return to detail page', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
      cy.get('[data-cy="contact-cases"]').should('exist');
      cy.get('[data-cy="contact-cases"]').click();
      cy.location('pathname').should('include', 'health-department/contact-cases/case-list');
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').should('exist');
      cy.get('[data-cy="input-lastname"]').should('exist');
      cy.get('[data-cy="input-dayofbirth"]').should('exist');
      cy.get('[data-cy="input-phone"]').should('exist');
      cy.get('[data-cy="input-email"]').should('exist');

      cy.get('[data-cy="input-firstname"]').type('Jack');
      cy.get('[data-cy="input-lastname"]').type('Randel');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('jack@gmail.com');

      cy.get('[data-cy="client-submit-button"] button').click();
      cy.wait('@newContact').its('status').should('eq', 201);
      cy.get('@newContact')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');

          cy.location('pathname').should('include', 'health-department/case-detail/contact/' + caseId + '/edit');
        });

      cy.get('[data-cy="start-tracking-button"]').should('be.disabled');
      cy.get('[data-cy="analog-tracking-button"]').should('be.disabled');
    });
  });
});
