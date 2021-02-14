/// <reference types="cypress" />

describe('enrollment external zip code', () => {
  beforeEach(() => {
    cy.server();
    cy.route('PUT', '/enrollment/details?confirmed=true').as('updatePersonalDetailsZipcodeConfirm');
    cy.route('PUT', '/enrollment/details').as('updatePersonalDetails');

    cy.logInNotEnrolledClient2();
  });

  describe('confirm external zip code in basic data', () => {
    it.skip('form completion', () => {
      cy.location('pathname').should('eq', '/client/enrollment/basic-data');
      cy.get('[data-cy="first-step-button"] button').should('be.disabled');
      cy.get('[data-cy="street-input"] input[matInput]').type('Höllentalstr.');
      cy.get('[data-cy="house-number-input"] input[matInput]').type('49');
      cy.get('[data-cy="zip-code-input"] input[matInput]').type('79110');
      cy.get('[data-cy="city-input"] input[matInput]').type('Buchenbach');
      cy.get('[data-cy="first-step-button"] button').should('be.enabled');
      cy.get('[data-cy="first-step-button"] button').click();
      cy.wait('@updatePersonalDetails').its('status').should('eq', 422);
      cy.get('[data-cy="confirm-button"]').should('exist');
      cy.get('[data-cy="confirm-button"]').click();
      cy.wait('@updatePersonalDetailsZipcodeConfirm').its('status').should('eq', 200);
      cy.location('pathname').should('eq', '/client/enrollment/health-department');
      cy.get('[data-cy="contact-button"]').should('not.exist');
      cy.get('[data-cy="profile-user-button"]').should('not.exist');
      cy.get('[data-cy="health-department-name"]').should('have.text', 'Landratsamt Breisgau-Hochschwarzwald');
    });
  });

  describe('login forbidden after external zip code confirmation', () => {
    it.skip('login', () => {
      cy.location('pathname').should('include', '/auth/forbidden');
      cy.get('[data-cy="forbidden-message"]').should(
        'have.text',
        'Für Sie ist ein anderes Gesundheitsamt zuständig. Wenden Sie sich bitte an dieses!'
      );
    });
  });
});
