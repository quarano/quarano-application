describe('login', () => {
  const login = (username: string, password: string) => {
    cy.get('#submitBtn button').should('be.disabled');
    cy.get('#username').type(username);

    cy.get('#submitBtn button').should('be.disabled');
    cy.get('#password').type(password);

    cy.get('#submitBtn button').should('be.enabled');
    cy.get('#submitBtn button').click();
  };

  beforeEach(() => {
    cy.server();
    cy.route('POST', '/login').as('logIn');
    cy.route('GET', '/user/me').as('me');
    cy.route('GET', '/hd/cases').as('cases');
    cy.route('GET', '/enrollment').as('enrollment');
    cy.route('GET', '/diary').as('diary');

    cy.visit('/');
  });

  describe('client login', () => {
    it('should successfully log in to client page', () => {
      cy.location('pathname').should('eq', '/auth/login');

      login('test3', 'test123');

      cy.wait('@logIn').its('status').should('eq', 200);
      cy.get('simple-snack-bar > span').should('contain.text', 'Willkommen bei quarano');
      cy.wait('@me').its('status').should('eq', 200);
      cy.wait('@enrollment').its('status').should('eq', 200);
      cy.wait('@diary').its('status').should('eq', 200);

      cy.location('pathname').should('eq', '/client/diary/diary-list');
    });

    it('should fail to log in with incorrect credentials', () => {
      cy.location('pathname').should('eq', '/auth/login');

      login('test3', 'test1234');

      cy.wait('@logIn').its('status').should('eq', 401);
      cy.get('simple-snack-bar > span').should('contain.text', 'Benutzername oder Passwort falsch');

      cy.location('pathname').should('not.eq', '/client/diary/diary-list');
    });
  });

  describe('agent login', () => {
    it('should successfully log in to agent page', () => {
      cy.location('pathname').should('eq', '/auth/login');

      login('agent1', 'agent1');

      cy.wait('@logIn').its('status').should('eq', 200);
      cy.get('simple-snack-bar > span').should('contain.text', 'Willkommen bei quarano');
      cy.wait('@me').its('status').should('eq', 200);
      cy.wait('@cases').its('status').should('eq', 200);

      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
    });

    it('should fail to log in with incorrect credentials', () => {
      cy.location('pathname').should('eq', '/auth/login');

      login('agent1', 'agent2');

      cy.wait('@logIn').its('status').should('eq', 401);
      cy.get('simple-snack-bar > span').should('contain.text', 'Benutzername oder Passwort falsch');

      cy.location('pathname').should('not.eq', '/health-department/index-cases/case-list');
    });
  });
});
