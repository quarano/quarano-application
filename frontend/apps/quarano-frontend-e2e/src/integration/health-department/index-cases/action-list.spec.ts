/// <reference types="cypress" />

describe('health-department index cases action-list', () => {
  beforeEach(() => {
    cy.intercept('GET', '/hd/actions', (req) => {
      if (req.url.endsWith('/hd/actions')) {
        req.alias = 'allActions';
      } else {
        req.alias = 'userAction';
      }
    });
    cy.intercept('GET', '/user/me').as('me');
    cy.intercept('GET', '/hd/cases', (req) => {
      if (req.url.endsWith('/hd/cases/')) {
        req.alias = 'allCases';
      } else {
        req.alias = 'specificCase';
      }
    });
    cy.intercept('PUT', '/hd/actions/*/resolve').as('updateCaseActions');
    cy.logInAgent();
  });

  it.skip('G10.3-1: comment on and close action', () => {
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.wait('@me').its('response.statusCode').should('eq', 200);
    cy.wait('@allCases').its('response.statusCode').should('eq', 200);

    cy.get('[data-cy="action-list"]').click();
    cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'index-cases/action-list');

    cy.wait('@allActions').its('response.statusCode').should('eq', 200);

    cy.get('[data-cy="action-data-table"]').within(() => {
      cy.get('[role=rowgroup] .ag-row')
        .should('have.length.greaterThan', 0)
        .contains('Sperber')
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
    cy.get('[data-cy="action-comments"]').type('Anruf erledigt');
    cy.get('[data-cy="close-actions"]').click();
    cy.get('[data-cy="confirm-button"]').click();
    cy.wait('@updateCaseActions').its('response.statusCode').should('eq', 200);

    cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'index-cases/action-list');

    cy.get('[data-cy="action-data-table"]').within(() => {
      cy.get('[role=rowgroup] .ag-row').contains('Sperber').should('have.length', 0);
    });
  });
});
