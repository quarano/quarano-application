/// <reference types="cypress" />

describe('static pages', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/frontendtexts').as('frontendTexts');
    cy.visit('/');
  });

  describe('landing page index', () => {
    it.skip('should successfully load landing page index', () => {
      cy.visit(`client/enrollment/landing/index/abc`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  describe('landing page contact', () => {
    it.skip('should successfully load landing page contact', () => {
      cy.visit(`client/enrollment/landing/contact/abc`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  describe('terms', () => {
    it.skip('should successfully load terms', () => {
      cy.visit(`general/terms`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  describe('data protection', () => {
    it.skip('should successfully load data protection', () => {
      cy.visit(`general/data-protection`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  describe('imprint', () => {
    it.skip('should successfully load imprint', () => {
      cy.visit(`general/imprint`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });
});
