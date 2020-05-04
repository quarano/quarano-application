describe('tenant top navigation', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/api/user/me').as('me');

    cy.loginAgent();
  });

  it('should show a navigation', () => {
    cy.get('[data-cy="case-overview"]').should('exist');
    cy.get('[data-cy="action-overview"]').should('exist');
  });

  it('should have selected case-overview', () => {
    cy.get('[data-cy="case-overview"]').should('have.class', 'active');
    cy.get('[data-cy="action-overview"]').should('not.have.class', 'active');
  });

  it('should switch to actions on click', () => {

    cy.get('[data-cy="action-overview"]').click();
    cy.get('[data-cy="action-overview"]').should('have.class', 'active');
    cy.url().should('include', '/tenant-admin/actions');

  });

  it('should show the current logged in agent', () => {
    cy.wait('@me').its('status').should('eq', 200);

    cy.get('@me').then((request: any) => {

      const user = request.response?.body;
      console.log(user);
      const fullName = user.firstName + ' ' + user.lastName;
      const buttonLabel = `${fullName} (${user.healthDepartment.name})`;

      cy.get('[data-cy="profile-user-button"]').should('contain.text', buttonLabel);
    });
  });

  it('should open a context menu on profile button click', () => {
    cy.get('div.mat-menu-panel').should('not.exist');
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('div.mat-menu-panel').should('exist');

    cy.get('[data-cy="profile-button"]').should('not.exist');
    cy.get('[data-cy="logout-button"]').should('exist');
  });

  it('should logout user on logout button click', () => {
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('[data-cy="logout-button"]').click();
    cy.url().should('include', '/welcome/login');
  });
});
