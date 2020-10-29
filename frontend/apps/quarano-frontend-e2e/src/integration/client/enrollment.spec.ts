/// <reference types="cypress" />

describe('enrollment happy path', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/api/enrollment/completion?withoutEncounters=true').as('completeenrollment');
    cy.route('PUT', '/api/enrollment/questionnaire').as('updatequestionnaire');
    cy.route('PUT', '/api/enrollment/details').as('updatepersonaldetails');

    cy.loginNotEnrolledClient();
  });

  describe('basic data', () => {
    it('form completion', () => {
      cy.url().should('include', '/client/enrollment/basic-data');
      cy.get('[data-cy="first-step-button"] button').should('be.disabled');
      cy.get('[data-cy="street-input"] input[matInput]').type('Platz der Republik');
      cy.get('[data-cy="house-number-input"] input[matInput]').type('1');
      cy.get('[data-cy="zip-code-input"] input[matInput]').type('68159');
      cy.get('[data-cy="city-input"] input[matInput]').type('Berlin');
      cy.get('[data-cy="first-step-button"] button').should('be.enabled');
      cy.get('[data-cy="first-step-button"] button').click();
      cy.wait('@updatepersonaldetails').its('status').should('eq', 200);
      cy.get('[data-cy="second-step-button"] button').should('exist');
    });
  });

  describe('questionnaire', () => {
    it('form completion', () => {
      cy.url().should('include', '/client/enrollment/basic-data');
      cy.get('[data-cy="second-step-button"] button').should('be.disabled');
      cy.get('[data-cy="has-no-symptoms-option"]').click();
      cy.get('[data-cy="has-no-pre-existion-conditions-option"]').click();
      cy.get('[data-cy="no-medical-staff-option"]').click();
      cy.get('[data-cy="no-contact-option"]').click();
      cy.get('[data-cy="second-step-button"] button').should('be.enabled');
      cy.get('[data-cy="second-step-button"] button').click();
      cy.wait('@updatequestionnaire').its('status').should('eq', 200);
      cy.get('[data-cy="third-step-button"] button').should('exist');
    });
  });

  describe('retrospective contacts', () => {
    it('form completion', () => {
      cy.url().should('include', '/client/enrollment/basic-data');
      cy.get('[data-cy="third-step-button"] button').should('be.enabled');
      cy.get('[data-cy="third-step-button"] button').click();
      cy.get('[data-cy="confirm-button"]').should('exist');
      cy.get('[data-cy="confirm-button"]').click();
      cy.wait('@completeenrollment').its('status').should('eq', 200);
      cy.url().should('include', '/client/diary/diary-list');
      cy.get('[data-cy="diary-menu-item"]').should('exist');
      cy.get('[data-cy="contact-person-menu-item"]').should('exist');
    });
  });
});

describe('enrollment external zip code', () => {
  beforeEach(() => {
    cy.server();
    cy.route('PUT', '/api/enrollment/details?confirmed=true').as('updatepersonaldetailszipcodeconfirm');
    cy.route('PUT', '/api/enrollment/details').as('updatepersonaldetails');

    cy.loginNotEnrolledClient2();
  });

  describe('basic data', () => {
    it('form completion', () => {
      cy.url().should('include', '/client/enrollment/basic-data');
      cy.get('[data-cy="first-step-button"] button').should('be.disabled');
      cy.get('[data-cy="street-input"] input[matInput]').type('Höllentalstr.');
      cy.get('[data-cy="house-number-input"] input[matInput]').type('49');
      cy.get('[data-cy="zip-code-input"] input[matInput]').type('79110');
      cy.get('[data-cy="city-input"] input[matInput]').type('Buchenbach');
      cy.get('[data-cy="first-step-button"] button').should('be.enabled');
      cy.get('[data-cy="first-step-button"] button').click();
      cy.wait('@updatepersonaldetails').its('status').should('eq', 422);
      cy.get('[data-cy="confirm-button"]').should('exist');
      cy.get('[data-cy="confirm-button"]').click();
      cy.wait('@updatepersonaldetailszipcodeconfirm').its('status').should('eq', 200);
      cy.url().should('include', '/client/enrollment/health-department');
      cy.get('[data-cy="contact-button"]').should('not.exist');
      cy.get('[data-cy="profile-user-button"]').should('not.exist');
      cy.get('[data-cy="health-department-name"]')
        .should('exist')
        .should('have.text', 'Landratsamt Breisgau-Hochschwarzwald');
    });
  });
});
