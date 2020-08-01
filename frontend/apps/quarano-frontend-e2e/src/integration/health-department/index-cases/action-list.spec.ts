/// <reference types="cypress" />

describe('health-department index cases action-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/api/hd/actions').as('allactions');

    cy.loginAgent();
    cy.get('[data-cy="action-list"]').should('exist');
    cy.get('[data-cy="action-list"]').click();
  });

  describe('preconditions', () => {
    it('should be on the correct url', () => {
      cy.url().should('include', '/health-department/index-cases/action-list');
    });

    it('should have correct page components', () => {
      cy.get('[data-cy="action-data-table"]').should('exist');
    });
  });

  describe('case list', () => {
    it('should get a list of cases and display in table', () => {
      cy.wait('@allactions').its('status').should('eq', 200);
      cy.get('[data-cy="action-data-table"]').find('datatable-row-wrapper').should('have.length.greaterThan', 0);
    });

    it('should open selected case', () => {
      cy.get('[data-cy="action-data-table"]').find('datatable-row-wrapper').eq(2).click();
      cy.url().should('include', '/health-department/case-detail/').should('include', '?tab=1');
    });
  });
});
