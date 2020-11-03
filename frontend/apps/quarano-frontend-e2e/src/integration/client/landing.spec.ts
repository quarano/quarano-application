/// <reference types="cypress" />

describe('landing page', () => {
  const clientCode = 'af12bd44-70b3-44f1-82d6-79bfd31dd4f4';
  beforeEach(() => {
    cy.server();
  });

  describe('correct content is shown', () => {
    it('index person', () => {
      cy.visit(`client/enrollment/landing/index/${clientCode}`);
      cy.get('[data-cy="cta-button-index"]').should('exist');
    });

    it('contact person', () => {
      cy.visit(`client/enrollment/landing/contact/${clientCode}`);
      cy.get('[data-cy="cta-button-index"]').should('exist');
    });
  });

  describe('user is directed to the right page with correct client code', () => {
    it('index person', () => {
      cy.visit(`client/enrollment/landing/index/${clientCode}`);
      cy.get('[data-cy="cta-button-index"]').click();
      cy.url().should('include', `/client/enrollment/register/${clientCode}`);
      cy.get('[data-cy="input-client-code"] input[matInput]').should('exist').should('have.value', clientCode);
    });

    it('contact person', () => {
      cy.visit(`client/enrollment/landing/contact/${clientCode}`);
      cy.get('[data-cy="cta-button-index"]').click();
      cy.url().should('include', `/client/enrollment/register/${clientCode}`);
      cy.get('[data-cy="input-client-code"] input[matInput]').should('exist').should('have.value', clientCode);
    });
  });
});
