/// <reference types="cypress" />

xdescribe('static pages', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/frontendtexts').as('frontendTexts');
    cy.visit('/');
  });

  xdescribe('landing page index', () => {
    it('should successfully load landing page index', () => {
      cy.visit(`client/enrollment/landing/index/abc`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  xdescribe('landing page contact', () => {
    it('should successfully load landing page contact', () => {
      cy.visit(`client/enrollment/landing/contact/abc`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  xdescribe('terms', () => {
    it('should successfully load terms', () => {
      cy.visit(`general/terms`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  xdescribe('data protection', () => {
    it('should successfully load data protection', () => {
      cy.visit(`general/data-protection`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });

  xdescribe('imprint', () => {
    it('should successfully load imprint', () => {
      cy.visit(`general/imprint`);
      cy.wait('@frontendTexts').its('status').should('eq', 200);
      cy.get('qro-static-page > div').children().should('have.length.greaterThan', 0);
    });
  });
});
