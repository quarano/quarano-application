/// <reference types="cypress" />

describe('Adding new symptom to symptom diary', () => {
  beforeEach(() => {
    cy.loginClient();
    cy.server();
    cy.route('POST', '/api/diary').as('diary');
  });

  it('not possible, when no body temperature is selected.', () => {
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

  it('happy path', () => {
    cy.location("pathname").should('include', 'client/diary/diary-list');
    cy.get('[data-cy="add-diary-entry"]').click();
    cy.get('[data-cy="body-temperature"]').should("exist");
    cy.get('[data-cy="symptoms-select"]').should("exist");
    cy.get('[data-cy="other-symptoms"]').should("exist");
    cy.get('[data-cy="other-contacts"]').should("exist");
    cy.get('[data-cy="add-missing-contacts"]').should("exist");
    cy.get('[data-cy="save-diary-entry"] button').should("be.disabled");

    cy.get('[data-cy="body-temperature"]')
      .trigger("mousedown", {button: 0});

    cy.get('mat-slide-toggle').should('have.length', 8);
    cy.get('#mat-slide-toggle-1').should("exist");
    cy.get('#mat-slide-toggle-6').should("exist");
    cy.get('#mat-slide-toggle-1').click();
    cy.get('#mat-slide-toggle-6').click();

    cy.get('[data-cy="other-symptoms"] input').type("Ageusie");
    cy.get('mat-option').click();
    cy.get('[data-cy="other-symptoms"] input').type("Agnosie");
    cy.get('mat-option').click();
    cy.get('[data-cy="other-contacts"] input').type("Lora Laurer");
    cy.get('mat-option').click();

    cy.get('[data-cy="save-diary-entry"] button').should("be.enabled");
    cy.get('[data-cy="cancel-diary-entry"]').should("be.enabled");
    cy.get('[data-cy="save-diary-entry"] button').click();

    cy.wait('@diary').its('status').should('eq', 201);

    cy.get('@diary').its('request.body').then(body => {
      const bodyTemperature = body.bodyTemperature;
      const contacts = body.contacts;
      const symptoms = body.symptoms;
      const entryId = body.id;
      expect(bodyTemperature).to.eq(39.5);
      expect(contacts).to.have.length(1);
      expect(symptoms).to.have.length(4);
      expect(entryId).to.eq(null);
    });

    cy.location("pathname").should('include', 'client/diary/diary-list');

  });

});
