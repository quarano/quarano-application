/// <reference types="cypress" />

describe('health-department contact cases case-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/hd/cases').as('allcases');

    cy.loginAgent();
    cy.get('[data-cy="contact-cases"]').click();
  });

  describe('preconditions', () => {
    it('should be on the correct url', () => {
      cy.url().should('include', '/health-department/contact-cases/case-list');
    });

    it('should have correct page components', () => {
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="search-case-input"]').should('exist');
      cy.get('[data-cy="case-data-table"]').should('exist');
    });
  });

  describe('case list', () => {
    it('should get a list of cases and display in table', () => {
      cy.wait('@allcases').its('status').should('eq', 200);

      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
    });

    it('should filter cases', () => {
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
      cy.get('[data-cy="search-case-input"]').type('drogler');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
    });

    it('should open new case page on button click', () => {
      cy.get('[data-cy="new-case-button"]').click();
      cy.url().should('include', '/health-department/case-detail');
    });

    it('should open selected case', () => {
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(2).click();
      cy.url().should('include', '/health-department/case-detail');
    });
  });
});
