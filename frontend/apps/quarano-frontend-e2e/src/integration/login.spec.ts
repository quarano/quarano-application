describe('login', () => {
  const login = (username: string, password: string) => {
    cy.server();
    cy.route('POST', '/login').as('login');
    cy.get('#submitBtn button').should('be.disabled');
    cy.get('#username').type(username);

    cy.get('#submitBtn button').should('be.disabled');
    cy.get('#password').type(password);

    cy.get('#submitBtn button').should('be.enabled');
    cy.get('#submitBtn button').click();
  };

  beforeEach(() => {
    cy.visit('http://localhost:4200');
  });

  describe('client login', () => {
    it('should successful login to client page', () => {
      // Go to login page

      login('test3', 'test123');

      cy.wait('@login').its('status').should('eq', 200);
      cy.get('simple-snack-bar').children('span').should('contain', 'Willkommen bei quarano');

      cy.url().should('include', '/client/diary/diary-list');
    });

    it('should fail to login', () => {
      login('test3', 'test1234');

      cy.wait('@login').its('status').should('eq', 401);
      cy.get('simple-snack-bar').children('span').should('contain', 'Benutzername oder Passwort falsch');

      cy.wait(5);
      cy.url().should('not.include', '/client/diary/diary-list');
    });
  });

  describe('agent login', () => {
    it('should successful login to agent page', () => {
      login('agent1', 'agent1');

      cy.wait('@login').its('status').should('eq', 200);
      cy.get('simple-snack-bar').children('span').should('contain', 'Willkommen bei quarano');

      cy.wait(5);
      cy.url().should('include', '/health-department/index-cases/case-list');
    });

    it('should fail to login', () => {
      login('agent1', 'agent2');

      cy.wait('@login').its('status').should('eq', 401);
      cy.get('simple-snack-bar').children('span').should('contain', 'Benutzername oder Passwort falsch');

      cy.url().should('not.include', '/health-department/index-cases/case-list');
    });
  });
});
