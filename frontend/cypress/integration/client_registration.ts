it('Should register an new client', () => {
  const baseUrl = 'http://localhost:4200';

  // Go to registration
  cy.visit(baseUrl);
  cy.get('#client-register-btn').click();

  // General info page
  cy.get('#next-btn').click();

  // Naming page
  cy.get('#firstName-input').type('Alice');
  cy.get('#lastName-input').type('Alice');
  cy.get('#s1-next-btn').click();

  // Phone and zipcode page
  cy.get('#phone-input').type('+49 77 444 00 99');
  cy.get('#zip-code-input').type('67125');
  cy.get('#s2-next-btn').click();

  // Summary
  cy.get('#s3-next-btn').click();

  // Question click through
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-no').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#bool-answer-yes').click();
  cy.get('#next-question-btn').click();
  cy.get('#next-question-btn').click();
  cy.get('#first-report-finish-btn').click();

  // Finish registration
  // cy.get('#finish-reg-btn').click();

});
