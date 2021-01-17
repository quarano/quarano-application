/// <reference types="cypress" />

describe(
  'S9 - GAMA kann Kontaktfall in Indexfall umwandeln',
  {
    defaultCommandTimeout: 20000,
  },
  () => {
    before((done) => {
      cy.restartBackend(done);

      cy.server();
      cy.route('POST', '/hd/cases/?type=contact').as('newContact');
      cy.route('PUT', '/hd/cases/*').as('updateIndexCase');
    });

    it('new contact case is able to complete registration', () => {
      cy.logInAgent();

      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.get('[data-cy="contact-cases"]').click();

      cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

      cy.get('[data-cy="search-case-input"] input').type('Dorothea');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });

      cy.location('pathname').should('include', Cypress.env('health_department_url') + 'case-detail/contact/');

      cy.get('[data-cy="input-field-test-date"]').type('5.6.2020');
      cy.get('[data-cy="covid-test-result-true"]').click();

      cy.get('[data-cy="confirm-button"]').click();

      cy.get('simple-snack-bar button').click();

      cy.get('[data-cy="quarantine-start-input"]').type('6.6.2020');
      cy.get('[data-cy="quarantine-end-input"]').type('20.6.2020');
      cy.get('[data-cy="phone-number-input"]').type('0721123456');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="zip-code-input"]').type('68163');
      cy.get('[data-cy="street-input"]').type('Test');
      cy.get('[data-cy="house-number-input"]').type('1');
      cy.get('[data-cy="city-input"]').type('Mannheim');

      cy.get('[data-cy="client-submit-and-close-button"] button').click();
      cy.wait('@updateIndexCase').its('status').should('eq', 200);
      cy.get('@updateIndexCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
        });

      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
      cy.get('[data-cy="search-case-input"] input').type('Dorothea');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });
      cy.get('[data-cy="client-cancel-button"]').click();

      // 10-prüfe, ob Ursprungsfall noch korrekt angezeigt wird -> "Jamie Fraser" Siggi Seufert

      cy.get('[data-cy="search-case-input"] input').type('Siggi');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });

      cy.get('[data-cy="contacts-tab"]').click();
      cy.location('pathname').should('include', '/contacts');
      cy.get('qro-contact-list').find('.ag-center-cols-container > .ag-row').should('have.length', 2);
      cy.get('qro-contact-list').find('.ag-center-cols-container > .ag-row').eq(1).should('contain', 'Dorothea');

      cy.get('[data-cy="contact-cases"]').click();

      cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));
      cy.get('[data-cy="search-case-input"] input').type('Dorothea');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 0);

      cy.get('[data-cy="index-cases"]').click();

      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      // 8-prüfe, ob Aussage "Keine Daten zum Anzeigen vorhanden" kommt
    });
  }
);
