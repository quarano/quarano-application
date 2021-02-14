/// <reference types="cypress" />

describe('health-department top navigation', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/user/me').as('me');
    cy.route('GET', '/hd/cases').as('allCases');

    cy.logInAdmin();
  });

  it('navigation should work as expected', () => {
    cy.wait('@me').its('status').should('eq', 200);
    cy.get('@me').then((request: any) => {
      const user = request.response?.body;
      const fullName = user.firstName + ' ' + user.lastName;
      const buttonLabel = `${fullName} (${user.healthDepartment.name})`;

      cy.get('[data-cy="profile-user-button"]').should('contain.text', buttonLabel);
    });
    cy.wait('@allCases').its('status').should('eq', 200);

    cy.get('[data-cy="index-cases"]').should('have.class', 'active');
    cy.get('[data-cy="export"]').should('not.have.class', 'active');
    cy.get('[data-cy="contact-cases"]').should('not.have.class', 'active');
    cy.location('pathname').should('eq', Cypress.env('index_cases_url'));

    cy.get('[data-cy="contact-cases"]').click();
    cy.get('[data-cy="contact-cases"]').should('have.class', 'active');
    cy.location('pathname').should('eq', Cypress.env('contact_cases_url'));

    cy.get('[data-cy="export"]').click();
    cy.get('[data-cy="export"]').should('have.class', 'active');
    cy.location('pathname').should('eq', Cypress.env('health_department_url') + 'export');

    cy.get('[data-cy="account-administration"]').click();
    cy.get('[data-cy="account-administration"]').should('have.class', 'active');
    cy.location('pathname').should('eq', '/administration/accounts/account-list');
  });

  it('user profile should work as expected', () => {
    cy.wait('@me').its('status').should('eq', 200);
    cy.wait('@allCases').its('status').should('eq', 200);
    cy.get('@me').then((request: any) => {
      const user = request.response?.body;
      const fullName = user.firstName + ' ' + user.lastName;
      const buttonLabel = `${fullName} (${user.healthDepartment.name})`;

      cy.get('[data-cy="profile-user-button"]').should('contain.text', buttonLabel);
    });

    cy.get('div.mat-menu-panel').should('not.exist');
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('div.mat-menu-panel').should('exist');

    cy.get('[data-cy="profile-button"]').should('not.exist');
    cy.get('[data-cy="change-password-button"]').should('exist');
    cy.get('[data-cy="logout-button"]').should('exist');

    cy.get('[data-cy="change-password-button"]').click();
    cy.location('pathname').should('eq', '/auth/change-password');
  });

  it('logout button should work as expected', () => {
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('[data-cy="logout-button"]').click();
    cy.location('pathname').should('eq', '/auth/login');
  });
});
