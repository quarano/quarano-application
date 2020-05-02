/// <reference types="cypress" />

describe('tenant actions overview', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/api/hd/actions'/*, 'fixture:get-api-hd-cases.json'*/).as('allactions');

    cy.loginAgent();
    cy.get('[data-cy="action-overview"]').click();
  });

  describe('preconditions', () => {
    it('should be on the correct url', () => {
      cy.url().should('include', '/tenant-admin/actions');
    });

    /*it('should have correct page components', () => {
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="export-cases-button"]').should('exist');
      cy.get('[data-cy="search-case-input"]').should('exist');
      cy.get('[data-cy="case-data-table"]').should('exist');
    });*/
  });
});
