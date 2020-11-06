/// <reference types="cypress" />

import * as moment from 'moment';

describe('csv export', () => {
  beforeEach(() => {
    cy.server();
    const dateString = moment().format('YYYY-MM-DD');
    cy.route('GET', `/hd/quarantines?from=${dateString}&to=${dateString}`).as('getcsv');

    cy.loginAgent();
    cy.visit('health-department/export');
  });

  describe('csv export happy path', () => {
    it('should get csv data', () => {
      cy.get('[data-cy="export-submit"] button').should('exist').should('be.enabled').click();
      cy.wait('@getcsv').its('status').should('eq', 200);
    });
  });
});
