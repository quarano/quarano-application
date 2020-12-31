/// <reference types="cypress" />

describe('csv export', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/export/quarantines').as('getcsv');

    cy.loginAgent();
    cy.visit('health-department/export');
  });

  describe('csv export happy path', () => {
    it('should get csv data', () => {
      cy.get('[data-cy="export-submit"] button').should('exist').should('be.enabled').click();
      cy.wait('@getcsv').its('status').should('eq', 200);
    });
  });
});
