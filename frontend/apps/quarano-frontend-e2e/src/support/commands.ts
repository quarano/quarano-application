// tslint:disable-next-line:no-namespace
declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command to select DOM element by data-cy attribute.
     * @example cy.dataCy('greeting')
     */
    logIn: (username: string, password: string) => void;
    logInAgent: () => void;
    logInAdmin: () => void;
    logInClient: () => void;

    logInNotEnrolledClient: () => void;
    logInNotEnrolledClient2: () => void;

    logOut: () => void;

    restart: (done: (err?: any) => void) => void;
  }
}

const logOut = () => {
  cy.get('[data-cy="profile-user-button"]').click();
  cy.get('[data-cy="logout-button"]').click();
  cy.location('pathname').should('include', 'auth/login');
};

const logIn = (username: string, password: string) => {
  cy.server();
  cy.route('POST', '/login').as('logIn');

  cy.visit('/', {
    onBeforeLoad(win) {
      cy.stub(win, 'open').as('windowOpen');
    },
  });

  cy.get('#username').type(username);
  cy.get('#password').type(password);
  cy.get('#submitBtn').click();

  cy.wait('@logIn');
};

Cypress.Commands.add('logOut', () => {
  logOut();
});
Cypress.Commands.add('logIn', (username: string, password: string) => {
  logIn(username, password);
});
Cypress.Commands.add('logInAgent', () => {
  logIn('agent1', 'agent1');
});
Cypress.Commands.add('logInAdmin', () => {
  logIn('admin', 'admin');
});
Cypress.Commands.add('logInClient', () => {
  logIn('test3', 'test123');
});
Cypress.Commands.add('logInNotEnrolledClient', () => {
  logIn('DemoAccount', 'DemoPassword');
});
Cypress.Commands.add('logInNotEnrolledClient2', () => {
  logIn('secUser2', 'secur1tyTest!');
});

Cypress.Commands.add('restart', (done: (err?: any) => void) => {
  Cypress.config('defaultCommandTimeout', 20000); // temporarily increase defaultCommandTimeout
  const req = () => {
    return fetch('http://localhost:8080/actuator/health/readiness')
      .then((response) => response.json())
      .then((response: any) => {
        if (response.status === 'UP') {
          Cypress.config('defaultCommandTimeout', 4000); // reset defaultCommandTimeout to default value

          done();
          return;
        }

        req();
      })
      .catch(() => {
        req();
      });
  };

  fetch('http://localhost:8080/actuator/restart', { method: 'POST' }).then(() => {
    cy.log('restart server');
    req();
  });
});
