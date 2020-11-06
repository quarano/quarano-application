/// <reference types="cypress" />

describe('csv export', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/hd/quarantines/*').as('getcsv');

    cy.loginAgent();
  });

  describe('csv export happy path', () => {
    it('should get csv data', () => {
      cy.get('[data-cy="export-submit"]').should('exist').should('be.enabled').click();
      cy.wait('@getcsv').its('status').should('eq', 200);

      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').should('have.length.greaterThan', 0);
    });
  });
});
