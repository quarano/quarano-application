/// <reference types="cypress" />

xdescribe('health-department index cases case-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/hd/cases').as('allCases');
    cy.route('PUT', '/hd/cases/*').as('saveDetails');
    cy.route('GET', '/user/me').as('me');
    cy.route('GET', '/hd/cases/*').as('case');

    cy.logInAgent();
  });

  it('should be able to select specific entry', () => {
    cy.wait('@me').its('status').should('eq', 200);
    cy.wait('@allCases').its('status').should('eq', 200);

    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);

    cy.get('[data-cy="search-index-case-input"]').type('hanser');
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length', 1)
      .eq(0)
      .click();

    cy.wait('@case').its('status').should('eq', 200);
    cy.get('@case')
      .its('response.body')
      .then(($body) => {
        const caseId = $body.caseId;
        cy.location('pathname').should(
          'include',
          Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/edit'
        );
      });
  });

  it('should add address', () => {
    cy.get('[data-cy="case-data-table"]').find('div[role="row"]').should('have.length.greaterThan', 0);
    cy.get('[data-cy="search-index-case-input"]').type('Harry');
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length', 1)
      .eq(0)
      .click();
    cy.location('pathname').should('include', '/edit');
    cy.get("[data-cy='street-input']").type('Frankfurterstrasse');
    cy.get("[data-cy='house-number-input']").type('11');
    cy.get("[data-cy='zip-code-input']").type('68163');
    cy.get("[data-cy='city-input']").type('Mannheim');
    cy.get("[data-cy='client-submit-and-close-button'] button").click();
    cy.wait('@saveDetails');
    cy.get('@saveDetails').its('status').should('eq', 200);
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
  });
});
