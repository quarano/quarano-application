/// <reference types="cypress" />

describe('health-department index cases', () => {
  beforeEach(() => {
    cy.loginAgent();
    cy.route('POST', '/hd/cases/?type=index').as('newIndex');
  });

  describe('creating new index case', () => {
    it('should not be possible if mandatory fields are missing', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
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
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
    });

    it('happy path: save and close', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
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
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('james.fraser@gmail.com');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');

      cy.get('[data-cy="client-submit-and-close-button"] button').click();
      cy.wait('@newIndex').its('status').should('eq', 201);
      cy.get('@newIndex')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
        });
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
    });

    it('happy path: save and check e-mail template', () => {
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
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

      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('james.fraser@gmail.com');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');

      cy.get('[data-cy="client-submit-button"] button').click();
      cy.wait('@newIndex').its('status').should('eq', 201);
      cy.get('@newIndex')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
          cy.location('pathname').should('include', 'health-department/case-detail/index/' + caseId + '/edit');
          cy.get('[data-cy="start-tracking-button"]').should('be.enabled');
          cy.get('[data-cy="analog-tracking-button"]').should('be.disabled');

          cy.get('[data-cy="start-tracking-button"]').click();
          cy.location('pathname').should('include', 'health-department/case-detail/index/' + caseId + '/email');
          cy.get('div.mail-text pre').should('exist');
          cy.get('div.mail-text pre').contains('Sehr geehrte/geehrter Frau/Herr Fraser');
          cy.get('[data-cy="copy-to-clipboard"]').should('exist');
          cy.get('[data-cy="copy-to-clipboard"]').click();
          cy.get('simple-snack-bar').should('exist');
        });
    });
  });
});
