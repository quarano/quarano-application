/// <reference types="cypress" />
/**
 1-Click auf  ‚"neuen Indexfalle anlegen"
 2-Vorname  ‘Jamie‘
 3-Nachname  "Fraser"
 4-Insert Geburtsdatum as "01.01.1970"
 5-Insert telephon  number as ‚162156156156‘
 6-Insert email as ‚james.fraser@gmail.com‘
 7-Click Auf Speichern
 CHECK 1: Neue Tabs erscheinen
 CHECK 2: "Nachverfolung starten" Button wird aktiv
 8-click auf  'Nachverfolgung Starten' Button
 CHECK 2: "Tab Emailvorlage" wird automatisch angezeigt
 9- click on 'in die zwischenablage kopieren' Button
 10. Extrahiere Anmeldelink aus dem Template
 11. Abmelden klicken
 CHECK: Benutzer ist auf Login Seite
 CHECK: Rechts oben wird kein Name mehr angezeigt
 12. Anmeldelink aufrufen
 CHECK: Benutzer seiht Willkommensseite für Indexfälle "Herzlich Willkommen bei quarano..."
 CHECK: "Registrieren" Button ist inaktiv
 13. Klick auf Weiter
 14. Benutzername: "Jamie"
 15. Passwort: "Password01!"
 16. Password bestätgen  "Password01!"
 17. Geburtsdatum: 01.01.1970
 18. AGB aktivieren
 19. Klick auf "Registrieren" Button
 CHECK: Benutzer ist auf der ersten Seiter der Initialen Datenerfassung
 CHECK: Rechts oben wird der name des Benutzers angezeigt: "Jamie Fraser"
 */
describe('S2 - Neu erstellter Indexfall kann sich registrieren', () => {
  // before((done) => {
  //   cy.restart(done);
  // });
  it.only('should run', () => {
    cy.logInAgent();
    cy.route('POST', '/hd/cases/?type=index').as('newIndex');

    /**
     * CHECK 1: Neue Tabs erscheinen (1/2)
     */
    cy.get('[data-cy="action-list"]').should('exist');
    cy.get('[data-cy="case-list"]').should('exist');

    /**
     *  1-Click auf  ‚"neuen Indexfalle anlegen"
     */
    cy.get('[data-cy="new-case-button"]').should('exist');
    cy.get('[data-cy="new-case-button"]').click();

    cy.location('pathname').should('eq', '/health-department/case-detail/new/index/edit');

    cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');

    /**
     *  2-Vorname  ‘Jamie‘
     */
    cy.get('[data-cy="input-firstname"]').type('Jamie');

    /**
     *  3-Nachname  "Fraser"
     */
    cy.get('[data-cy="input-lastname"]').type('Fraser');

    /**
     *  4-Insert Geburtsdatum as "01.01.1970"
     */
    cy.get('[data-cy="input-dayofbirth"]').type('01.01.1970');

    /**
     *  5-Insert telephon  number as ‚162156156156‘
     */
    cy.get('[data-cy="input-phone"]').type('162156156156');

    /**
     *  6-Insert email as ‚james.fraser@gmail.com‘
     */
    cy.get('[data-cy="input-email"]').type('james.fraser@gmail.com');

    cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
    cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');

    /**
     *  7-Click Auf Speichern
     */
    cy.get('[data-cy="client-submit-button"] button').click();

    cy.wait('@newIndex').its('status').should('eq', 201);
    cy.get('@newIndex')
      .its('response.body')
      .then((body) => {
        const caseId = body.caseId;
        expect(caseId).not.to.eq(null);
        expect(caseId).not.to.eq('');
        cy.location('pathname').should('eq', '/health-department/case-detail/index/' + caseId + '/edit');
      });

    /**
     * CHECK 2: "Nachverfolung starten" Button wird aktiv
     */
    cy.get('[data-cy="start-tracking-button"]').should('exist');
    cy.get('[data-cy="start-tracking-button"]').should('be.enabled');

    /**
     * CHECK 1: Neue Tabs erscheinen (2/2)
     * CHECK 2: "Tab Emailvorlage" wird automatisch angezeigt (1/2)
     */
    cy.get('.mat-tab-links').children().should('have.length', 5);

    /**
     * 8-click auf  'Nachverfolgung Starten' Button
     */
    cy.get('[data-cy="start-tracking-button"]').click();

    /**
     * CHECK 2: "Tab Emailvorlage" wird automatisch angezeigt (2/2)
     */
    cy.get('.mat-tab-links').children().should('have.length', 6);

    cy.location('pathname').should('include', '/comments');

    cy.get('[data-cy="email-tab"]').click();

    cy.location('pathname').should('include', '/email');

    /**
     * 9- click on 'in die zwischenablage kopieren' Button
     */
    cy.get('[data-cy="copy-to-clipboard"]').click();

    cy.get('[data-cy="mail-text"]').then((elem) => {
      const regex = /\/client\/enrollment\/landing\/index\/(.*)/g;
      let content;

      /**
       * 10. Extrahiere Anmeldelink aus dem Template (1/2)
       */
      if (typeof elem !== 'string') {
        content = elem.text();
      } else {
        content = elem;
      }

      /**
       * 10. Extrahiere Anmeldelink aus dem Template (2/2)
       */
      const code = regex.exec(content)[1];

      cy.logOut();

      /**
       * CHECK: Benutzer ist auf Login Seite
       */
      cy.location('pathname').should('eq', '/auth/login');

      /**
       * CHECK: Rechts oben wird kein Name mehr angezeigt (2/2)
       */
      cy.get('[data-cy="profile-user-button"]').should('not.exist');

      /**
       * 12. Anmeldelink aufrufen
       */
      cy.visit('/client/enrollment/landing/index/' + code);

      /**
       * CHECK: Benutzer sieht Willkommensseite für Indexfälle "Herzlich Willkommen bei quarano..."
       */
      cy.get('h1 strong').should('contain.text', 'quarano');
      cy.location().should((loc) => {
        // expect(loc.toString()).to.eq(url);
      });

      cy.get('[data-cy="cta-button-index"]').should('exist');

      /**
       * 13. Klick auf Weiter
       */
      cy.get('[data-cy="cta-button-index"]').click();

      /**
       * CHECK: "Registrieren" Button ist inaktiv
       */
      cy.get('[data-cy="registration-submit-button"]').should('exist');
      cy.get('[data-cy="registration-submit-button"] button').should('be.disabled');

      /**
       * 14. Benutzername: "Jamie"
       */
      cy.get('[data-cy="input-username"] input[matInput]').type('Jamie');

      /**
       * 15. Passwort: "Password01!"
       */
      cy.get('[data-cy="input-password"] input[matInput]').type('Password01!');

      /**
       * 16. Password bestätgen  "Password01!"
       */
      cy.get('[data-cy="input-password-confirm"] input[matInput]').type('Password01!');

      /**
       * 17. Geburtsdatum: 01.01.1970
       */
      cy.get('[data-cy="input-dateofbirth"] input[matInput]').type('01.01.1970');

      /**
       * 18. AGB aktivieren
       */
      cy.get('[data-cy="input-privacy-policy"]').click();
      cy.get('[data-cy="registration-submit-button"] button').should('be.enabled');

      /**
       * 19. Klick auf "Registrieren" Button
       */
      cy.get('[data-cy="registration-submit-button"] button').click();

      /**
       * CHECK: Benutzer ist auf der ersten Seiter der Initialen Datenerfassung
       */
      cy.location('pathname').should('eq', '/client/enrollment/basic-data');

      /**
       * CHECK: Rechts oben wird der name des Benutzers angezeigt: "Jamie Fraser"
       */
      cy.get('[data-cy="profile-user-button"] .mat-button-wrapper span').should('have.text', 'Jamie Fraser ');
    });
  });
});
