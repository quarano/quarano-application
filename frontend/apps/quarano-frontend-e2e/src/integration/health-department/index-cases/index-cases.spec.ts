/// <reference types="cypress" />

describe('health-department index cases', () => {
  beforeEach(() => {
    cy.loginAgent();
    cy.route('POST', '/hd/cases/?type=index').as('newIndex');
    cy.route('GET', '/hd/cases/*').as('getCase');
    cy.route('GET', '/hd/cases/*/diary').as('diary');
  });

  describe('creating new index case', () => {
    it('should not be possible if mandatory fields are missing', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').should('exist');
      cy.get('[data-cy="input-lastname"]').should('exist');
      cy.get('[data-cy="input-dayofbirth"]').should('exist');
      cy.get('[data-cy="input-phone"]').should('exist');
      cy.get('[data-cy="input-email"]').should('exist');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
    });

    it('happy path: save and close', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').should('exist');
      cy.get('[data-cy="input-lastname"]').should('exist');
      cy.get('[data-cy="input-dayofbirth"]').should('exist');
      cy.get('[data-cy="input-phone"]').should('exist');
      cy.get('[data-cy="input-email"]').should('exist');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('james.fraser@gmail.com');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');

      cy.get('[data-cy="client-submit-and-close-button"] button').click();
      cy.wait('@newIndex').its('status').should('eq', 201);
      cy.get('@newIndex')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
        });
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
    });

    it('happy path: save and check e-mail template', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
      cy.get('[data-cy="new-case-button"]').should('exist');
      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').should('exist');
      cy.get('[data-cy="input-lastname"]').should('exist');
      cy.get('[data-cy="input-dayofbirth"]').should('exist');
      cy.get('[data-cy="input-phone"]').should('exist');
      cy.get('[data-cy="input-email"]').should('exist');

      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');
      cy.get('[data-cy="input-phone"]').type('162156156156');
      cy.get('[data-cy="input-email"]').type('james.fraser@gmail.com');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');

      cy.get('[data-cy="client-submit-button"] button').click();
      cy.wait('@newIndex').its('status').should('eq', 201);
      cy.get('@newIndex')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          expect(caseId).not.to.eq(null);
          expect(caseId).not.to.eq('');
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/edit');
          cy.get('[data-cy="start-tracking-button"]').should('be.enabled');
          cy.get('[data-cy="analog-tracking-button"]').should('be.disabled');

          cy.get('[data-cy="start-tracking-button"]').click();
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/comments');
        });
    });
  });

  describe('viewing case details of existing index case', () => {
    it.skip('should show diary entries', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');

      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('exist');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(1).click();

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/edit');
        });

      cy.get('[data-cy="case-detail-diary-link"]').should('exist');
      cy.get('[data-cy="case-detail-diary-link"]').click();

      cy.wait('@diary').its('status').should('eq', 200);
      cy.get('@diary')
        .its('response.body')
        .then((body) => {
          const entries = body._embedded.trackedCaseDiaryEntrySummaryList;
          expect(entries).to.have.length(3);
          entries.forEach((entry) => {
            expect(entry.bodyTemperature).to.not.eq(null);
            expect(entry.reportedAt).to.not.eq(null);
            expect(entry.slot.timeOfDay === 'morning' || entry.slot.timeOfDay === 'evening').to.eq(true);
            expect(entry.symptoms).to.not.eq(null);
            expect(entry.contacts).to.not.eq(null);
          });
        });

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/diary');
        });

      cy.get('qro-diary-entries-list').should('exist');
      const entryListItems = cy.get('qro-diary-entries-list-item');
      entryListItems.should('have.length', 2);

      const yesterday = new Date();
      yesterday.setDate(new Date().getDate() - 1);
      const twoDaysAgo = new Date();
      twoDaysAgo.setDate(yesterday.getDate() - 1);
      const dateYesterday =
        yesterday.toLocaleDateString('de', { weekday: 'short' }) + '., ' + yesterday.toLocaleDateString('de');
      const dateTwoDaysAgo =
        twoDaysAgo.toLocaleDateString('de', { weekday: 'short' }) + '., ' + twoDaysAgo.toLocaleDateString('de');

      entryListItems.each(($elem, $index, $elems) => {
        const entryContainers = cy.wrap($elem).find('.entry-container');
        entryContainers.should('have.length', 3);
        if ($index === 0) {
          cy.wrap($elems[0]).find('span[data-cy="entry-date"]').should('have.text', dateTwoDaysAgo);
          cy.wrap($elems[0]).find('span[data-cy="no-entry"]').should('exist');
          cy.wrap($elems[0]).find('span[data-cy="temperature-evening"]').should('have.text', '35,8 °C');
          cy.wrap($elems[0]).find('span[data-cy="contacts-evening"]').should('not.exist');
          cy.wrap($elems[0]).find('span[data-cy="symptoms-evening"]').should('have.text', 'Husten, Nackenschmerzen');
        } else {
          cy.wrap($elems[1]).find('span[data-cy="entry-date"]').should('have.text', dateYesterday);
          cy.wrap($elems[1]).find('span[data-cy="no-entry"]').should('not.exist');
          cy.wrap($elems[1]).find('span[data-cy="temperature-morning"]').should('have.text', '36,5 °C');
          cy.wrap($elems[1])
            .find('span[data-cy="contacts-morning"]')
            .should('have.text', 'Sonja Sortig, Manuel Mertens, unbekannte Kontaktperson');
          cy.wrap($elems[1]).find('span[data-cy="symptoms-morning"]').should('not.exist');
          cy.wrap($elems[1]).find('span[data-cy="temperature-evening"]').should('have.text', '36,5 °C');
          cy.wrap($elems[1])
            .find('span[data-cy="contacts-evening"]')
            .should('have.text', 'Sonja Sortig, Manuel Mertens, unbekannte Kontaktperson');
          cy.wrap($elems[1]).find('span[data-cy="symptoms-evening"]').should('have.text', 'Husten');
        }
      });
    });

    it.skip('should display message if no diary entries exist', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');

      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('exist');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(0).click();

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/edit');
        });

      cy.get('[data-cy="case-detail-diary-link"]').should('exist');
      cy.get('[data-cy="case-detail-diary-link"]').click();

      cy.wait('@diary').its('status').should('eq', 200);
      cy.get('@diary')
        .its('response.body')
        .then((body) => {
          const entries = body._embedded.trackedCaseDiaryEntrySummaryList;
          expect(entries).to.have.length(0);
        });

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/diary');
        });

      cy.get('qro-diary-entries-list').should('exist');
      cy.get('qro-diary-entries-list-item').should('have.length', 0);
      cy.get('[data-cy="no-entries"]').should('exist');
    });
  });
});
