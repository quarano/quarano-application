/// <reference types="cypress" />

import * as dayjs from 'dayjs';
import 'dayjs/locale/de';
import * as localeData from 'dayjs/plugin/localeData';

dayjs.locale('de');
dayjs.extend(localeData);

describe(
  'S8 - Initiale Datenerfassung und Retrospektive Kontaktanlage funktioniert',
  {
    defaultCommandTimeout: 20000,
  },
  () => {
    before((done) => {
      cy.restartBackend(done);
    });

    beforeEach(() => {
      cy.intercept('POST', '/enrollment/completion?withoutEncounters=false').as('completeEnrollment');
      cy.intercept('PUT', '/enrollment/questionnaire').as('updateQuestionnaire');
      cy.intercept('PUT', '/enrollment/details').as('updatePersonalDetails');
      cy.intercept('GET', '/diary').as('diary');
      cy.intercept('GET', '/details').as('details');
      cy.intercept('GET', '/contacts').as('contacts');
      cy.intercept('GET', '/hd/cases', (req) => {
        if (req.url.endsWith('/questionnaire')) {
          req.alias = 'questionnaireForCase';
        } else {
          req.alias = 'specificCase';
        }
      });
    });

    describe('enroll as client', () => {
      it('personal data', () => {
        cy.logInNotEnrolledClient();

        cy.location('pathname').should('eq', '/client/enrollment/basic-data');

        cy.get('[data-cy="first-step-button"] button').should('be.disabled');
        cy.get('[data-cy="personal-data-firstName"] input').should('have.value', 'Markus');
        cy.get('[data-cy="personal-data-lastName"] input').should('have.value', 'Hanser');
        cy.get('[data-cy="personal-data-dateOfBirth"] input').should('have.value', '1.1.1990');
        cy.get('[data-cy="personal-data-email"] input').should('have.value', 'markus.hanser@testtest.de');
        cy.get('[data-cy="personal-data-phone"] input').should('have.value', '0621222255');
        cy.get('[data-cy="street-input"]').type('Hauptstraße');
        cy.get('[data-cy="house-number-input"]').type('15');
        cy.get('[data-cy="zip-code-input"]').type('68199');
        cy.get('[data-cy="city-input"]').type('Mannheim');
        cy.get('[data-cy="first-step-button"] button').click();
        cy.wait('@updatePersonalDetails').its('response.statusCode').should('eq', 200);
      });

      it('initial questionnaire', () => {
        cy.get('[data-cy="second-step-button"] button').should('be.disabled');
        cy.get('[data-cy="has-symptoms-option"]').click();

        const tenDaysAgo = dayjs().subtract(10, 'days').format('DD.MM.YYYY');

        cy.get('[data-cy="dayOfFirstSymptoms"]').type(tenDaysAgo);
        cy.get('[data-cy="characteristicSymptoms"]').type('Fever');
        cy.get('mat-option').click();
        cy.get('[data-cy="familyDoctor"]').type('Dr Schmidt');
        cy.get('[data-cy="has-pre-existing-conditions-option"]').click();
        cy.get('[data-cy="hasPreExistingConditionsDescription"]').type('test');
        cy.get('[data-cy="medical-staff-option"]').click();
        cy.get('[data-cy="belongToMedicalStaffDescription"]').type('Merck');
        cy.get('[data-cy="contact-option"]').click();
        cy.get('[data-cy="hasContactToVulnerablePeopleDescription"]').type('Peter Aalen');
        cy.get('[data-cy="second-step-button"] button').click();
        cy.wait('@updateQuestionnaire').its('response.statusCode').should('eq', 200);
      });

      it('contacts', () => {
        cy.location('pathname').should('eq', '/client/enrollment/basic-data');
        cy.get('mat-horizontal-stepper').find('fieldset').should('have.length.greaterThan', 13);
        cy.get('[data-cy="multiple-auto-complete-input"]').eq(1).type('Claire Fraser').blur();
        cy.get('[data-cy="confirm-button"]').click();

        cy.get('qro-contact-person-dialog').should('exist');
        cy.get('[data-cy="contact-person-form-phone"] input')
          .click()
          .then(($elem) => {
            cy.wrap($elem).click().type('123123123123');
          });
        cy.get('[data-cy="submit-button"] button').click();

        cy.get('[data-cy="multiple-auto-complete-input"]').eq(3).type('Claire Fraser');
        cy.get('mat-option').click();

        cy.get('[data-cy="third-step-button"] button').click();
        cy.wait('@completeEnrollment').its('response.statusCode').should('eq', 200);
        cy.wait('@diary').its('response.statusCode').should('eq', 200);
        cy.location('pathname').should('eq', '/client/diary/diary-list');
        cy.get('[data-cy="diary-menu-item"]').should('exist');
        cy.get('[data-cy="contact-person-menu-item"]').should('exist');
      });

      it('check client profile page', () => {
        cy.get('[data-cy="profile-user-button"]').click();
        cy.get('[data-cy="profile-button"]').click();
        cy.wait('@details').its('response.statusCode').should('eq', 200);
        cy.location('pathname').should('include', '/client/profile');

        cy.get('[data-cy="personal-data-firstName"] input').should('have.value', 'Markus');
        cy.get('[data-cy="personal-data-lastName"] input').should('have.value', 'Hanser');
        cy.get('[data-cy="personal-data-dateOfBirth"] input').should('have.value', '1.1.1990');
        cy.get('[data-cy="personal-data-email"] input').should('have.value', 'markus.hanser@testtest.de');
        cy.get('[data-cy="personal-data-phone"] input').should('have.value', '0621222255');

        cy.get('[data-cy="street-input"] input').should('have.value', 'Hauptstraße');
        cy.get('[data-cy="house-number-input"] input').should('have.value', '15');
        cy.get('[data-cy="zip-code-input"] input').should('have.value', '68199');
        cy.get('[data-cy="city-input"] input').should('have.value', 'Mannheim');

        cy.get('[data-cy="contact-person-menu-item"]').click();
        cy.wait('@contacts').its('response.statusCode').should('eq', 200);
        cy.location('pathname').should('eq', '/client/contact-persons/contact-person-list');
        cy.get('mat-card').should('contain', 'Claire Fraser');

        cy.logOut();
      });
    });

    describe('check results GAMA', () => {
      it('check edit page', () => {
        cy.logInAgent();

        cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
        cy.get('[data-cy="search-index-case-input"]').type('Markus');
        cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .then(($elems) => {
            $elems[0].click();
          });
        cy.location('pathname').should('include', '/edit');

        cy.get('.mat-tab-links').children().should('have.length', 6);

        cy.wait('@specificCase').its('response.statusCode').should('eq', 200);
        cy.get('@specificCase')
          .its('response.body')
          .then(($body) => {
            expect($body.caseId).to.not.eq(null);
            expect($body.caseId).to.not.eq('');
          });

        cy.get('[data-cy="input-firstname"] input').should('have.value', 'Markus');
        cy.get('[data-cy="input-lastname"] input').should('have.value', 'Hanser');
        cy.get('[data-cy="input-dayofbirth"] input').should('have.value', '1.1.1990');
        cy.get('[data-cy="input-email"] input').should('have.value', 'markus.hanser@testtest.de');
        cy.get('[data-cy="input-phone"] input').should('have.value', '0621222255');

        cy.get('[data-cy="street-input"] input').should('have.value', 'Hauptstraße');
        cy.get('[data-cy="house-number-input"] input').should('have.value', '15');
        cy.get('[data-cy="input-zipcode"] input').should('have.value', '68199');
        cy.get('[data-cy="city-input"] input').should('have.value', 'Mannheim');

        const today = dayjs().format('D.M.YYYY');
        const twoDaysAgo = dayjs().subtract(2, 'days').format('D.M.YYYY');
        const twoWeeksFromNow = dayjs().add(14, 'days').format('D.M.YYYY');

        cy.get('[data-cy="input-testdate"] input').should('have.value', twoDaysAgo);
        cy.get('[data-cy="input-quarantinestart"] input').should('have.value', today);
        cy.get('[data-cy="input-quarantineend"]').find('input').should('have.value', twoWeeksFromNow);
      });

      it('check contacts', () => {
        cy.get('[data-cy="contacts-tab"]').click();
        cy.location('pathname').should('include', '/contacts');
        cy.wait('@contacts').its('response.statusCode').should('eq', 200);
        cy.get('@contacts')
          .its('response.body')
          .then((body) => {
            expect(body._embedded.contacts[0].firstName).to.eq('Claire');
            expect(body._embedded.contacts[0].lastName).to.eq('Fraser');
          });
        cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
      });

      it('check questionnaire', () => {
        cy.get('[data-cy="questionnaire-tab"]').click();
        cy.location('pathname').should('include', '/questionnaire');
        cy.wait('@questionnaireForCase').its('response.statusCode').should('eq', 200);

        const tenDaysAgo = dayjs().subtract(10, 'days').format('YYYY-MM-DD');

        cy.get('[data-cy="symptoms-date"]').should('have.text', tenDaysAgo);
        cy.get('[data-cy="familyDoctor"]').should('have.text', 'Dr Schmidt');
        cy.get('[data-cy="presumed-origin"]').should('have.text', 'Nicht angegeben');
        cy.get('[data-cy="presumed-date"]').should('have.text', 'Nicht angegeben');
        cy.get('[data-cy="pre-existing-conditions"]').should('have.text', ' test '); // TODO: spaces
        cy.get('[data-cy="belongToMedicalStaffDescription"]').should('have.text', 'Merck');
        cy.get('[data-cy="hasContactToVulnerablePeopleDescription"]').should('have.text', ' Peter Aalen '); // TODO: spaces
      });

      it('check associated contact', () => {
        cy.get('[data-cy="contact-cases"]').click();

        cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

        cy.get('[data-cy="search-contact-case-input"] input').type('Claire');
        cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(0).click();
        cy.wait('@specificCase').its('response.statusCode').should('eq', 200);
        cy.get('@specificCase')
          .its('response.body')
          .then(($body) => {
            expect($body._embedded.originCases[0].firstName).to.eq('Markus');
            expect($body._embedded.originCases[0].lastName).to.eq('Hanser');
          });
        cy.location('pathname').should('include', '/edit');
        // cy.get('[data-cy="lazy-autocomplete-chip-list"]').contains('Hanser, Markus');
      });
    });
  }
);
