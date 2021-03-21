/// <reference types="cypress" />

describe('health-department contact cases action-list', () => {
  beforeEach(() => {
    cy.intercept({
      method: 'GET',
      path: /^\/hd\/actions$/,
    }).as('allActions');

    cy.intercept({
      method: 'GET',
      path: /^\/hd\/actions\/\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b$/,
    }).as('userAction');

    cy.intercept('GET', '/user/me').as('me');

    cy.intercept({
      method: 'GET',
      path: /^\/hd\/cases\/$/,
    }).as('allCases');

    cy.intercept({
      method: 'GET',
      path: /^\/hd\/cases\/\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b$/,
    }).as('specificCase');

    cy.intercept('PUT', '/hd/actions/*/resolve').as('updateCaseActions');

    cy.logInAgent();
  });

  it('G10.3-2: comment on and close action', () => {
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.wait('@me').its('response.statusCode').should('eq', 200);
    cy.wait('@allCases').its('response.statusCode').should('eq', 200);

    cy.get('[data-cy="contact-cases"]').click();
    cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

    cy.get('[data-cy="action-list"]').click();
    cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'contact-cases/action-list');

    cy.wait('@allActions').its('response.statusCode').should('eq', 200);

    cy.get('[data-cy="action-data-table"]').within(() => {
      cy.get('[role=rowgroup] .ag-row')
        .should('have.length.greaterThan', 0)
        .contains('Thomas')
        .should('have.length', 1)
        .eq(0)
        .parent()
        .click();
    });

    cy.wait('@specificCase').its('response.statusCode').should('eq', 200);
    cy.wait('@userAction').its('response.statusCode').should('eq', 200);
    cy.get('@userAction')
      .its('response.body')
      .then((body) => {
        expect(body.caseId).not.to.eq(null);
        expect(body.numberOfResolvedAnomalies).to.eq(0);
        expect(body.numberOfUnresolvedAnomalies).to.eq(1);
      });

    cy.location('pathname').should('include', '/actions');
    cy.get('qro-client-action').should('exist');
    cy.get('qro-client-action-anomaly').contains('Körpertemperatur 39,9°C übersteigt Grenzwert 37,9°C');

    cy.get('[data-cy="action-comments"]').type('Test empfohlen');
    cy.get('[data-cy="close-actions"]').click();
    cy.get('[data-cy="confirm-button"]').click();
    cy.wait('@updateCaseActions').its('response.statusCode').should('eq', 200);

    cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'contact-cases/action-list');

    cy.get('[data-cy="action-data-table"]').within(() => {
      cy.get('[role=rowgroup] .ag-row').contains('Thomas').should('have.length', 0);
    });
  });
});
