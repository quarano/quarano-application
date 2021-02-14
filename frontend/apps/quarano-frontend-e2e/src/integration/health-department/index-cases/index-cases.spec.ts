/// <reference types="cypress" />

import * as dayjs from 'dayjs';
import 'dayjs/locale/de';
import * as localeData from 'dayjs/plugin/localeData';
dayjs.locale('de');
dayjs.extend(localeData);

xdescribe('health-department index cases', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/cases/?type=index').as('newIndex');
    cy.route('GET', '/hd/cases/*').as('getCase');
    cy.route('GET', '/hd/cases/*/diary').as('diary');
    cy.route('GET', '/hd/cases/').as('getCases');
    cy.route('GET', '/user/me').as('me');
    cy.logInAgent();
  });

  xdescribe('creating new index case', () => {
    it('should not be possible if mandatory fields are missing', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.wait('@getCases').its('status').should('eq', 200);
      cy.wait('@me').its('status').should('eq', 200);

      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

      cy.get('[data-cy="input-firstname"]').type('Jamie');
      cy.get('[data-cy="input-lastname"]').type('Fraser');
      cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
    });

    it('happy path: save and close', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.wait('@getCases').its('status').should('eq', 200);
      cy.wait('@me').its('status').should('eq', 200);

      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

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
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
    });

    it('happy path: save and check e-mail template', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.wait('@getCases').its('status').should('eq', 200);
      cy.wait('@me').its('status').should('eq', 200);

      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');
      cy.get('[data-cy="client-cancel-button"]').should('be.enabled');

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
          expect(body.caseId).not.to.eq(null);
          expect(body.caseId).not.to.eq('');
          expect(body.caseType).to.eq('index');
          expect(body.caseTypeLabel).to.eq('Index');
          expect(body.city).to.eq(null);
          expect(body.comments).to.be.an('array').that.does.have.length(0);
          expect(body.contactCount).to.eq(0);
          expect(body.dateOfBirth).to.eq('1970-01-01');
          expect(body.email).to.eq('james.fraser@gmail.com');
          expect(body.extReferenceNumber).to.eq(null);
          expect(body.firstName).to.eq('Jamie');
          expect(body.houseNumber).to.eq(null);
          expect(body.indexContacts).to.be.an('array').that.does.have.length(0);
          expect(body.infected).to.be.eq(true);
          expect(body.lastName).to.eq('Fraser');
          expect(body.locale).to.eq(null);
          expect(body.mobilePhone).to.eq(null);
          expect(body.openAnomaliesCount).to.eq(1);
          expect(body.phone).to.eq('162156156156');
          expect(body.status).to.eq('angelegt');
          expect(body.street).to.eq(null);
          expect(body.zipCode).to.eq(null);
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/edit'
          );
          cy.get('[data-cy="start-tracking-button"]').should('be.enabled');
          cy.get('[data-cy="analog-tracking-button"]').should('be.disabled');

          cy.get('[data-cy="start-tracking-button"]').click();
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/comments'
          );
        });
    });
  });

  xdescribe('viewing case details of existing index case', () => {
    it('should show diary entries', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
      cy.get('[data-cy="search-index-case-input"]').type('Nadine');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/edit'
          );
        });

      cy.get('[data-cy="case-detail-diary-link"]').should('exist');
      cy.get('[data-cy="case-detail-diary-link"]').click();

      cy.wait('@diary').its('status').should('eq', 200);
      cy.get('@diary')
        .its('response.body')
        .then((body) => {
          const entries = body._embedded.trackedCaseDiaryEntrySummaryList;
          expect(entries).to.have.length(4);
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
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/diary'
          );
        });

      cy.get('qro-diary-entries-list').should('exist');
      const entryListItems = cy.get('qro-diary-entries-list-item');
      entryListItems.should('have.length', 3);

      const today = dayjs().format('DD.MM.YYYY');
      const weekdays = dayjs.localeData().weekdaysShort();
      const todaysDateString = weekdays[dayjs().get('day')] + ', ' + today;

      const yesterday = dayjs().subtract(1, 'day').format('DD.MM.YYYY');
      const yesterdayDateString = weekdays[dayjs().subtract(1, 'day').get('day')] + ', ' + yesterday;

      const twoDaysAgo = dayjs().subtract(2, 'days').format('DD.MM.YYYY');
      const twoDaysAgoDateString = weekdays[dayjs().subtract(2, 'days').get('day')] + ', ' + twoDaysAgo;

      entryListItems.each(($elem, $index, $elems) => {
        const entryContainers = cy.wrap($elem).find('.entry-container');
        entryContainers.should('have.length', 3);
        if ($index === 0) {
          cy.wrap($elems[0]).find('span[data-cy="entry-date"]').should('have.text', todaysDateString);
          cy.wrap($elems[0]).find('span[data-cy="no-entry"]').should('not.exist');
          cy.wrap($elems[0]).find('span[data-cy="temperature-morning"]').should('have.text', '36,5 °C');
          cy.wrap($elems[0])
            .find('span[data-cy="contacts-morning"]')
            .should('have.text', 'Sonja Sortig, Manuel Mertens, unbekannte Kontaktperson, ? Meier, Peter ?');
          cy.wrap($elems[0]).find('span[data-cy="symptoms-morning"]').should('not.exist');

          cy.wrap($elems[0]).find('span[data-cy="temperature-evening"]').should('have.text', '35,8 °C');
          cy.wrap($elems[0]).find('span[data-cy="contacts-evening"]').should('not.exist');
          cy.wrap($elems[0]).find('span[data-cy="symptoms-evening"]').should('have.text', 'Husten, Nackenschmerzen');
        } else if ($index === 1) {
          cy.wrap($elems[1]).find('span[data-cy="entry-date"]').should('have.text', yesterdayDateString);
          cy.wrap($elems[1])
            .find('span[data-cy="no-entry"]')
            .should('exist')
            .should('have.text', 'Kein Eintrag vorhanden');

          cy.wrap($elems[1]).find('span[data-cy="temperature-evening"]').should('have.text', '36,5 °C');
          cy.wrap($elems[1])
            .find('span[data-cy="contacts-evening"]')
            .should('have.text', 'Sonja Sortig, Manuel Mertens, unbekannte Kontaktperson, ? Meier, Peter ?');
          cy.wrap($elems[1]).find('span[data-cy="symptoms-evening"]').should('have.text', 'Husten');
        } else {
          cy.wrap($elems[2]).find('span[data-cy="entry-date"]').should('have.text', twoDaysAgoDateString);
          cy.wrap($elems[2])
            .find('span[data-cy="no-entry"]')
            .should('exist')
            .should('have.text', 'Kein Eintrag vorhanden');

          cy.wrap($elems[2]).find('span[data-cy="temperature-evening"]').should('have.text', '35,5 °C');
          cy.wrap($elems[2])
            .find('span[data-cy="contacts-evening"]')
            .should('have.text', 'Sonja Sortig, Manuel Mertens, unbekannte Kontaktperson, ? Meier, Peter ?');
          cy.wrap($elems[2]).find('span[data-cy="symptoms-evening"]').should('have.text', 'Husten, Nackenschmerzen');
        }
      });
    });

    it('should display message if no diary entries exist', () => {
      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('exist');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .should('have.length.greaterThan', 0);
      cy.get('[data-cy="search-index-case-input"]').type('Peter');
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });

      cy.wait('@getCase').its('status').should('eq', 200);
      cy.get('@getCase')
        .its('response.body')
        .then((body) => {
          const caseId = body.caseId;
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/edit'
          );
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
          cy.location('pathname').should(
            'eq',
            Cypress.env('health_department_url') + 'case-detail/index/' + caseId + '/diary'
          );
        });

      cy.get('qro-diary-entries-list').should('exist');
      cy.get('qro-diary-entries-list-item').should('have.length', 0);
      cy.get('[data-cy="no-entries"]').should('exist');
    });
  });
});
