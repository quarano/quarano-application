// tslint:disable-next-line:no-namespace
declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command to select DOM element by data-cy attribute.
     * @example cy.dataCy('greeting')
     */
    loginAgent: () => void;
    loginClient: () => void;
    loginNotEnrolledClient: () => void;
  }
}

const login = (username: string, password: string) => {
  cy.server();
  cy.route('POST', '/login').as('login');

  cy.visit('http://localhost:4200', {
    onBeforeLoad(win) {
      cy.stub(win, 'open').as('windowOpen');
    },
  });

  cy.get('#username').type(username);
  cy.get('#password').type(password);
  cy.get('#submitBtn').click();

  cy.wait('@login');
};

Cypress.Commands.add('loginAgent', () => {
  login('agent1', 'agent1');
});
Cypress.Commands.add('loginClient', () => {
  login('test3', 'test123');
});
Cypress.Commands.add('loginNotEnrolledClient', () => {
  login('DemoAccount', 'DemoPassword');
});
