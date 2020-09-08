/// <reference types="cypress" />

describe('Adding new symptom to symptom diary', () => {
  beforeEach(() => {
    cy.loginClient();
  });

    it('fails when no body temperature is selected.', () => {
      cy.location("pathname").should('include', 'client/diary/diary-list');
      cy.get('[data-cy="add-diary-entry"]').click();
      cy.get('[data-cy="body-temperature"]').should("exist");
      cy.get('[data-cy="symptoms-select"]').should("exist");
      cy.get('[data-cy="other-symptoms"]').should("exist");
      cy.get('[data-cy="other-contacts"]').should("exist");
      cy.get('[data-cy="add-missing-contacts"]').should("exist");

      cy.get('[data-cy="save-diary-entry"] button').should("be.disabled");
      cy.get('[data-cy="cancel-diary-entry"]').should("be.enabled");

    });

});
