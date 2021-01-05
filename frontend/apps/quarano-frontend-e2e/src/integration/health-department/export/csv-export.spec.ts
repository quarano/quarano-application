/// <reference types="cypress" />

describe('csv export', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/export/quarantines').as('getCsv');

    cy.logInAgent();
  });

  describe('csv export happy path', () => {
    it('should get csv data', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
      cy.get('[data-cy="export"]').click();
      cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'export');
      cy.get('[data-cy="export-submit"] button').should('be.enabled').click();
      cy.wait('@getCsv').its('status').should('eq', 200);
    });
  });
});
