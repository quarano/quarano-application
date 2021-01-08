/// <reference types="cypress" />

describe('S8 - Initiale Datenerfassung und Retrospektive Kontaktanlage funktioniert', () => {
  Cypress.config('defaultCommandTimeout', 20000);
  before((done) => {
    cy.restartBackend(done);

    cy.server();
    cy.route('POST', '/enrollment/completion?withoutEncounters=false').as('completeEnrollment');
    cy.route('PUT', '/enrollment/questionnaire').as('updateQuestionnaire');
    cy.route('PUT', '/enrollment/details').as('updatePersonalDetails');
    cy.route('GET', '/diary').as('diary');
  });

  it('can complete enrollment and retrospective', () => {
    cy.logInNotEnrolledClient();

    cy.location('pathname').should('eq', '/client/enrollment/basic-data');

    cy.get('[data-cy="first-step-button"] button').should('be.disabled');
    cy.get('[data-cy="street-input"] input[matInput]').type('Hauptstraße');
    cy.get('[data-cy="house-number-input"] input[matInput]').type('15');
    cy.get('[data-cy="zip-code-input"] input[matInput]').type('68199');
    cy.get('[data-cy="city-input"] input[matInput]').type('Mannheim');
    cy.get('[data-cy="first-step-button"] button').should('be.enabled');
    cy.get('[data-cy="first-step-button"] button').click();
    cy.wait('@updatePersonalDetails').its('status').should('eq', 200);

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
    cy.get('[data-cy="second-step-button"] button').should('be.enabled');
    cy.get('[data-cy="second-step-button"] button').click();
    cy.wait('@updateQuestionnaire').its('status').should('eq', 200);

    cy.location('pathname').should('eq', '/client/enrollment/basic-data');
    cy.get('mat-horizontal-stepper').find('fieldset').should('have.length.greaterThan', 13);
    cy.get('[data-cy="multiple-auto-complete-input"]').eq(1).type('Claire Fraser').blur();
    cy.get('[data-cy="confirm-button"]').click();

    cy.get('qro-contact-person-form')
      .should('exist')
      .find('mat-form-field[data-cy="contact-person-form-phone"]')
      .then((fields) => {
        cy.wrap(fields[0]).type('123123123123');
      });
    cy.get('[data-cy="submit-button"] button').click();

    cy.get('[data-cy="multiple-auto-complete-input"]').eq(3).type('Claire Fraser');
    cy.get('mat-option').click();

    cy.get('[data-cy="third-step-button"] button').should('be.enabled');
    cy.get('[data-cy="third-step-button"] button').click();
    cy.wait('@completeEnrollment').its('status').should('eq', 200);
    cy.wait('@diary').its('status').should('eq', 200);

    cy.location('pathname').should('eq', '/client/diary/diary-list');

    cy.get('[data-cy="diary-menu-item"]').should('exist');
    cy.get('[data-cy="contact-person-menu-item"]').should('exist');
  });
});
