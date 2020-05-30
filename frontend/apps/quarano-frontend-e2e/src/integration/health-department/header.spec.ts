describe('health-department top navigation', () => {
  beforeEach(() => {
    cy.server();
    cy.route('GET', '/api/user/me').as('me');

    cy.loginAgent();
  });

  it('should show a navigation', () => {
    cy.get('[data-cy="index-cases"]').should('exist');
    cy.get('[data-cy="contact-cases"]').should('exist');
  });

  it('should have selected index cases', () => {
    cy.get('[data-cy="index-cases"]').should('have.class', 'active');
    cy.get('[data-cy="contact-cases"]').should('not.have.class', 'active');
  });

  it('should switch to contact cases on click', () => {

    cy.get('[data-cy="contact-cases"]').click();
    cy.get('[data-cy="contact-cases"]').should('have.class', 'active');
    cy.url().should('include', '/health-department/contact-cases/case-list');

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
    cy.get('[data-cy="change-password-button"]').should('exist');
    cy.get('[data-cy="logout-button"]').should('exist');
  });

  it('should logout user on logout button click', () => {
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('[data-cy="logout-button"]').click();
    cy.url().should('include', '/auth/login');
  });

  it('should navigate to change password component on button click', () => {
    cy.get('[data-cy="profile-user-button"]').click();
    cy.get('[data-cy="change-password-button"]').click();
    cy.url().should('include', 'change-password');
  })
});
