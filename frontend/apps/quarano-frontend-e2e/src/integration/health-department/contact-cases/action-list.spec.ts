/// <reference types="cypress" />

describe('health-department contact cases action-list', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/hd/actions').as('allActions');
    cy.route('GET', '/user/me').as('me');
    cy.route('GET', '/hd/cases/').as('cases');
    cy.route('GET', '/hd/actions/*').as('action');
    cy.route('GET', '/hd/cases/*').as('case');
    cy.loginAgent();
  });

  it('load actions', () => {
    cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
    cy.wait('@me').its('status').should('eq', 200);
    cy.wait('@cases').its('status').should('eq', 200);
    cy.get('[data-cy="contact-cases"]').click();
    cy.location('pathname').should('eq', '/health-department/contact-cases/case-list');
    cy.get('[data-cy="action-list"]').click();
    cy.location('pathname').should('eq', '/health-department/contact-cases/action-list');
    cy.wait('@allActions').its('status').should('eq', 200);
    cy.get('[data-cy="action-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);
    cy.get('[data-cy="action-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .then(($elems) => {
        $elems[1].click();
      });
    cy.location('pathname').should('include', '/health-department/case-detail/contact');
    cy.wait('@action').its('status').should('eq', 200);
    cy.wait('@case').its('status').should('eq', 200);
    cy.get('qro-client-action').should('exist');
    cy.get('@action')
      .its('response.body')
      .then((body) => {
        expect(body.caseId).not.to.eq(null);
        expect(body.numberOfResolvedAnomalies).to.eq(0);
        expect(body.numberOfUnresolvedAnomalies).to.eq(1);
      });
    cy.get('qro-client-action-anomaly').contains('Fehlende Stammdaten (KP).');
  });
});
