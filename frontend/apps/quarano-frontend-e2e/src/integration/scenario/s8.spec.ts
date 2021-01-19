/// <reference types="cypress" />

describe(
  'S8 - Initiale Datenerfassung und Retrospektive Kontaktanlage funktioniert',
  {
    defaultCommandTimeout: 20000,
  },
  () => {
    before((done) => {
      cy.restartBackend(done);

      cy.intercept('POST', '/enrollment/completion?withoutEncounters=false').as('completeEnrollment');
      cy.intercept('PUT', '/enrollment/questionnaire').as('updateQuestionnaire');
      cy.intercept('PUT', '/enrollment/details').as('updatePersonalDetails');
      cy.intercept('GET', '/diary').as('diary');
      cy.intercept('GET', '/details').as('details');
      cy.intercept('GET', '/contacts').as('contacts');
      cy.intercept('GET', '/hd/cases/*').as('case');
      cy.intercept('GET', '/hd/cases/*/questionnaire').as('questionnaire');
    });

    it('can complete enrollment and retrospective', () => {
      cy.logInNotEnrolledClient();

      cy.location('pathname').should('eq', '/client/enrollment/basic-data');

      cy.get('[data-cy="first-step-button"] button').should('be.disabled');
      cy.get('[data-cy="street-input"]').type('Hauptstraße');
      cy.get('[data-cy="house-number-input"]').type('15');
      cy.get('[data-cy="zip-code-input"]').type('68199');
      cy.get('[data-cy="city-input"]').type('Mannheim');
      cy.get('[data-cy="first-step-button"] button').click();
      cy.wait('@updatePersonalDetails').its('response.statusCode').should('eq', 200);

      cy.get('[data-cy="second-step-button"] button').should('be.disabled');
      cy.get('[data-cy="has-symptoms-option"]').click();

      const today = new Date();
      const tenDaysAgo = new Date();
      tenDaysAgo.setDate(today.getDate() - 10);

      cy.get('[data-cy="dayOfFirstSymptoms"]').type(tenDaysAgo.toLocaleDateString('de-DE'));
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

      cy.location('pathname').should('eq', '/client/enrollment/basic-data');
      cy.get('mat-horizontal-stepper').find('fieldset').should('have.length.greaterThan', 13);
      cy.get('[data-cy="multiple-auto-complete-input"]').eq(1).type('Claire Fraser').blur();
      cy.get('[data-cy="confirm-button"]').click();

      cy.get('qro-contact-person-dialog').should('exist');
      cy.get('[data-cy="contact-person-form-phone"] input')
        .click()
        .then(($elem) => {
          cy.wrap($elem).type('123123123123');
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

      cy.logInAgent();

      cy.location('pathname').should('eq', Cypress.env('index_cases_url'));
      cy.get('[data-cy="search-case-input"]').type('Markus');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);
      cy.get('[data-cy="case-data-table"]')
        .find('.ag-center-cols-container > .ag-row')
        .then(($elems) => {
          $elems[0].click();
        });
      cy.location('pathname').should('include', '/edit');

      cy.get('.mat-tab-links').children().should('have.length', 6);

      cy.wait('@case').its('response.statusCode').should('eq', 200);
      cy.get('@case')
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

      const twoDaysAgo = new Date();
      twoDaysAgo.setDate(today.getDate() - 2);
      const twoWeeksFromNow = new Date();
      twoWeeksFromNow.setDate(today.getDate() + 14);

      cy.get('[data-cy="input-testdate"] input').should('have.value', twoDaysAgo.toLocaleDateString('de'));
      cy.get('[data-cy="input-quarantinestart"] input').should('have.value', today.toLocaleDateString('de'));
      cy.get('[data-cy="input-quarantineend"]')
        .find('input')
        .should('have.value', twoWeeksFromNow.toLocaleDateString('de'));

      cy.get('[data-cy="contacts-tab"]').click();
      cy.location('pathname').should('include', '/contacts');
      cy.wait('@contacts').its('response.statusCode').should('eq', 200);
      cy.get('@contacts')
        .its('response.body')
        .then((body) => {
          expect(body).to.be.an('array').that.does.have.length(1);
          expect(body[0].firstName).to.eq('Claire');
          expect(body[0].lastName).to.eq('Fraser');
        });
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').should('have.length', 1);

      cy.get('[data-cy="questionnaire-tab"]').click();
      cy.location('pathname').should('include', '/questionnaire');
      cy.wait('@questionnaire').its('response.statusCode').should('eq', 200);

      // cy.get('[data-cy="symptoms-date"]').should('have.text', tenDaysAgo.toLocaleDateString()); TODO: check once dayjs is merged
      cy.get('[data-cy="familyDoctor"]').should('have.text', 'Dr Schmidt');
      cy.get('[data-cy="presumed-origin"]').should('have.text', 'Nicht angegeben');
      cy.get('[data-cy="presumed-date"]').should('have.text', 'Nicht angegeben');
      cy.get('[data-cy="pre-existing-conditions"]').should('have.text', ' test '); // TODO: spaces
      cy.get('[data-cy="belongToMedicalStaffDescription"]').should('have.text', 'Merck');
      cy.get('[data-cy="hasContactToVulnerablePeopleDescription"]').should('have.text', ' Peter Aalen '); // TODO: spaces

      cy.get('[data-cy="contact-cases"]').click();

      cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

      cy.get('[data-cy="search-case-input"] input').type('Claire');
      cy.get('[data-cy="case-data-table"]').find('.ag-center-cols-container > .ag-row').eq(0).click();
      cy.location('pathname').should('include', '/edit');
      cy.wait('@case').its('response.statusCode').should('eq', 200);
      cy.get('@case')
        .its('response.body')
        .then(($body) => {
          expect($body.firstName).to.eq('Claire');
          expect($body.lastName).to.eq('Fraser');
        });
    });
  }
);
