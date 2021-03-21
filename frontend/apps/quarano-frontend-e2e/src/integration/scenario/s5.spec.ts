/// <reference types="cypress" />

import * as dayjs from 'dayjs';
import 'dayjs/locale/de';
import * as localeData from 'dayjs/plugin/localeData';

dayjs.locale('de');
dayjs.extend(localeData);

describe(
  'S5 - Einträge mit Fieber über 39,5 Grad bei Kontaktfall lösen eine Aktion aus',
  {
    defaultCommandTimeout: 20000,
  },
  () => {
    before((done) => {
      cy.restartBackend(done);
    });

    beforeEach(() => {
      cy.intercept('GET', '/hd/actions').as('allActions');
      cy.intercept('GET', '/hd/cases/').as('cases');
      cy.intercept('POST', '/diary').as('diary');
    });

    describe('crate action', () => {
      it('happy path', () => {
        cy.logInAgent();
        cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

        cy.wait('@cases').its('response.statusCode').should('eq', 200);

        cy.get('[data-cy="contact-cases"]').click();

        cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

        cy.get('[data-cy="action-list"]').click();
        cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'contact-cases/action-list');

        cy.wait('@allActions').its('response.statusCode').should('eq', 200);

        cy.get('[data-cy="action-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .should('have.length.greaterThan', 0);

        cy.get('[data-cy="action-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .contains('Julian Jäger')
          .should('have.length', 0);

        cy.get('[data-cy="contact-cases"]').click();

        cy.get('[data-cy="search-contact-case-input"] input').type('Julian');
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .then(($elems) => {
            $elems[0].click();
          });

        cy.get('[data-cy="case-detail-diary-link"]').click();
        cy.get('qro-diary-entries-list-item').contains('36,2 °C');
        cy.get('[data-cy="case-detail-actions-link"]').click();
        cy.get('qro-client-action').should('not.exist');

        cy.logOut();

        cy.logIn('testJulian', 'test123');

        cy.location('pathname').should('eq', '/client/diary/diary-list');

        cy.get('[data-cy="add-diary-entry"]').click();

        cy.get('[data-cy="save-diary-entry"] button').should('be.disabled');
        cy.get('[data-cy="body-temperature"]').trigger('mousedown', 'right', { button: 0 }).trigger('mouseup');
        cy.get('[data-cy="save-diary-entry"] button').click();

        cy.wait('@diary').its('response.statusCode').should('eq', 201);

        cy.location('pathname').should('eq', '/client/diary/diary-list');
        cy.logOut();

        cy.logInAgent();

        cy.get('[data-cy="contact-cases"]').click();

        cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

        cy.get('[data-cy="action-list"]').click();
        cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'contact-cases/action-list');

        cy.wait('@allActions').its('response.statusCode').should('eq', 200);

        cy.get('[data-cy="action-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .contains('SYMPTOME AUFFÄLLIG')
          .should('have.length', 1);

        cy.get('[data-cy="contact-list"]').click();
        cy.get('[data-cy="search-contact-case-input"] input').type('Julian');
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .then(($elems) => {
            $elems[0].click();
          });

        cy.get('[data-cy="case-detail-diary-link"]').click();
        cy.get('qro-diary-entries-list-item').should('have.length', 2);
        cy.get('qro-diary-entries-list-item').eq(0).contains('44,0 °C');
        cy.get('[data-cy="case-detail-actions-link"]').click();
        cy.get('qro-client-action-anomaly > ul > li').contains('Körpertemperatur 44,0°C übersteigt Grenzwert 37,9°C.');
      });
    });
  }
);
