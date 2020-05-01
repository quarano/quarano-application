// tslint:disable-next-line:no-namespace
declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command to select DOM element by data-cy attribute.
     * @example cy.dataCy('greeting')
     */
    loginAgent: () => void;
    loginClient: () => void;
  }
}

const login = (username: string, password: string) => {
  cy.visit( 'http://localhost:4200');

  cy.get('#username').type(username);
  cy.get('#password').type(password);
  cy.get('#submitBtn').click();

  cy.wait(5);
};

Cypress.Commands.add('loginAgent', () => {
  login('agent1', 'agent1');
});
Cypress.Commands.add('loginClient', () => {
  login('test3', 'test123');
});
