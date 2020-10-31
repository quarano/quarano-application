// tslint:disable-next-line:no-namespace
declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command to select DOM element by data-cy attribute.
     * @example cy.dataCy('greeting')
     */
    login: (username: string, password: string) => void;
    loginAgent: () => void;
    loginAdmin: () => void;
    loginClient: () => void;
    loginNotEnrolledClient: () => void;
    loginNotEnrolledClient2: () => void;
  }
}

const login = (username: string, password: string) => {
  cy.server();
  cy.route('POST', '/login').as('login');

  cy.visit('/', {
    onBeforeLoad(win) {
      cy.stub(win, 'open').as('windowOpen');
    },
  });

  cy.get('#username').type(username);
  cy.get('#password').type(password);
  cy.get('#submitBtn').click();

  cy.wait('@login');
};

Cypress.Commands.add('login', (username: string, password: string) => {
  login(username, password);
});
Cypress.Commands.add('loginAgent', () => {
  login('agent1', 'agent1');
});
Cypress.Commands.add('loginAdmin', () => {
  login('admin', 'admin');
});
Cypress.Commands.add('loginClient', () => {
  login('test3', 'test123');
});
Cypress.Commands.add('loginNotEnrolledClient', () => {
  login('DemoAccount', 'DemoPassword');
});
Cypress.Commands.add('loginNotEnrolledClient2', () => {
  login('secUser2', 'secur1tyTest!');
});
