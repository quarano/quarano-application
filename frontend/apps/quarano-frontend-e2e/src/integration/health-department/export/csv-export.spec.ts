/// <reference types="cypress" />

describe('csv export', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/export/quarantines').as('getCsv');

    cy.loginAgent();
  });

  describe('csv export happy path', () => {
    it('should get csv data', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
      cy.get('[data-cy="export"]').click();
      cy.location('pathname').should('eq', '/health-department/export');
      cy.get('[data-cy="export-submit"] button').should('exist').should('be.enabled').click();
      cy.wait('@getCsv').its('status').should('eq', 200);
    });
  });
});
