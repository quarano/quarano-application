/// <reference types="cypress" />

describe('health-department index cases case-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/api/hd/cases?type=index').as('allcases');
    cy.route('PUT', '/api/hd/cases/*').as('savedetails');

    cy.loginAgent();
  });

  describe('preconditions', () => {
    it('should be on the correct url', () => {
      cy.url().should('include', '/health-department/index-cases/case-list');
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

      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').should('have.length.greaterThan', 0);
    });

    it('should filter cases', () => {
      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').should('have.length.greaterThan', 0);
      cy.get('[data-cy="search-case-input"]').type('hanser');
      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').should('have.length', 1);
    });

    it('should open new case page on button click', () => {
      cy.get('[data-cy="new-case-button"]').click();
      cy.url().should('include', '/health-department/case-detail');
    });

    it('should open selected case', () => {
      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').eq(2).click();
      cy.url().should('include', '/health-department/case-detail');
    });

    it('should call mailto: selected case on click on mail icon', () => {
      cy.get('[data-cy="case-data-table"]').find('datatable-row-wrapper').eq(2).find('[data-cy="mail-button"]').click();

      cy.get('@windowOpen').should('be.calledWithMatch', 'mailto');
    });

    it('should add address', () => {
      cy.get('[data-cy="case-data-table"] datatable-row-wrapper').eq(0).click();
      cy.location('pathname').should('include', '/index/');
      cy.get("[data-cy='street-input']").type('Frankfurterstrasse');
      cy.get("[data-cy='house-number-input']").type('11');
      cy.get("[data-cy='zip-code-input']").type('60987');
      cy.get("[data-cy='city-input']").type('Frankfurt');
      cy.get("[data-cy='client-submit-and-close-button'] button").click();
      cy.wait('@savedetails');
      cy.get('@savedetails').its('status').should('eq', 200);
      cy.location('pathname').should('include', 'health-department/index-cases/case-list');
    });
  });
});
