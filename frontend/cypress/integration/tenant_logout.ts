  it('Should logout an health department user', () => {
    const baseUrl = 'http://localhost:4200';

    // Login the user
    cy.visit(baseUrl);
    cy.get('#tenant-admin-login-link').click();

    cy.get('#username-input').type('testhausen');
    cy.get('#password-input').type('test123');
    cy.get('#submit-btn').click();

    cy.get('#tenant-admin-logout').click();

    cy.url().should('eq', `${baseUrl}/tenant-admin/login`);
  });
