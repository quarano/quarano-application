/// <reference types="cypress" />

describe('health-department index cases case-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/hd/cases').as('allCases');
    cy.route('PUT', '/hd/cases/*').as('saveDetails');

    cy.loginAgent();
  });

  it('should be on the correct url', () => {
    cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
    cy.get('[data-cy="new-case-button"]').should('exist');
    cy.get('[data-cy="search-case-input"]').should('exist');
    cy.get('[data-cy="case-data-table"]').should('exist');
    cy.wait('@allCases').its('status').should('eq', 200);
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);
    cy.get('[data-cy="search-case-input"]').type('hanser');
    cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
    cy.get('[data-cy="new-case-button"]').click();
    cy.location('pathname').should('eq', '/health-department/case-detail/new/index/edit');
    // cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(2).click();
    // cy.location('pathname').should('include', '/edit');
    // cy.get('[data-cy="case-data-table"]')
    //   .find('.ag-center-cols-container > .ag-row')
    //   .eq(2)
    //   .find('[data-cy="mail-button"]')
    //   .click();
    // cy.get('@windowOpen').should('be.calledWithMatch', 'mailto');
  });

  it('should add address', () => {
    cy.get('[data-cy="case-data-table"]').find('div[role="row"]').should('have.length.greaterThan', 0);
    cy.get('div[row-index="1"] > div[role="gridcell"]')
      .should('have.length.greaterThan', 0)
      .then(($elems) => {
        $elems[0].click();
      });
    cy.location('pathname').should('include', '/edit');
    cy.get("[data-cy='street-input']").clear().type('Frankfurterstrasse');
    cy.get("[data-cy='house-number-input']").clear().type('11');
    cy.get("[data-cy='zip-code-input']").clear().type('60987');
    cy.get("[data-cy='city-input']").clear().type('Frankfurt');
    cy.get("[data-cy='client-submit-and-close-button'] button").click();
    cy.wait('@saveDetails');
    cy.get('@saveDetails').its('status').should('eq', 200);
    cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
  });
});
