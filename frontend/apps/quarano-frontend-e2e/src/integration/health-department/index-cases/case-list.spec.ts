/// <reference types="cypress" />

describe('health-department index cases case-list', () => {
  beforeEach(() => {
    cy.intercept('GET', '/hd/cases').as('allCases');
    cy.intercept('PUT', '/hd/cases/*').as('saveDetails');
    cy.intercept('GET', '/user/me').as('me');
    cy.intercept('GET', '/hd/cases/*').as('case');

    cy.logInAgent();
  });

  it('G02.4-7 - should be able to filter entries', () => {
    cy.wait('@me').its('response.statusCode').should('eq', 200);
    cy.wait('@allCases').its('response.statusCode').should('eq', 200);

    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);

    cy.get('[aria-colindex="4"] > .ag-cell-label-container > .ag-header-cell-menu-button > .ag-icon').click();

    cy.get('div.ag-filter').within((_) => {
      cy.get('input[type="date"]').eq(0).type('1979-12-31');
      cy.get('div.ag-picker-field-display').eq(0).click();
    });

    cy.get('div.ag-popup').find('div.ag-list-item.ag-select-list-item').eq(1).click();

    cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 10);

    cy.get('[aria-colindex="3"] > .ag-cell-label-container > .ag-header-cell-menu-button > .ag-icon').click();
    cy.get('div.ag-filter').within((_) => {
      cy.get('input[type="text"]').eq(0).type('xyz');
    });

    cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 0);

    cy.get('div.ag-filter').within((_) => {
      cy.get('input[type="text"]').eq(0).clear().type('i');
    });

    cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 3);

    cy.get('[data-cy="search-index-case-input"]').type('angelegt');
    cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
  });

  it('should be able to select specific entry', () => {
    cy.wait('@me').its('response.statusCode').should('eq', 200);
    cy.wait('@allCases').its('response.statusCode').should('eq', 200);

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

    cy.wait('@case').its('response.statusCode').should('eq', 200);
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
    cy.get('@saveDetails').its('response.statusCode').should('eq', 200);
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
  });
});
