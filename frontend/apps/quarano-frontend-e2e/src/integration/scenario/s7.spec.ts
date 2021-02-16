/// <reference types="cypress" />

describe('S7 - Status wechselt korrekt', () => {
  Cypress.config('defaultCommandTimeout', 20000);
  before((done) => {
    cy.restartBackend(done);
  });

  function extractActivationCode(elem: JQuery) {
    const regex = /\/client\/enrollment\/landing\/index\/(.*)/g;
    let content;

    if (typeof elem !== 'string') {
      content = elem.text();
    } else {
      content = elem;
    }

    try {
      return regex.exec(content)[1];
    } catch (e) {
      cy.log(e);
      throw e;
    }
  }

  it('should run', () => {
    /* 0 - Login als Gama "agent1" */
    cy.logInAgent();

    /* Route Definitionen */
    //TODO
    //cy.intercept('GET', '/hd/actions').as('createIndex');

    /* 1 - wähle Übersichtsseite "Indexfälle" */
    cy.get('[data-cy="index-cases"]').should('exist').click();

    /* 2 - wähle "neuen Indexfall anlegen" */
    cy.get('[data-cy="new-case-button"]').should('exist').click();

    /* 3 - Vorname -> "Berta" */
    cy.get('[data-cy="input-firstname"]').should('exist').type('Berta');

    /* 4 - Nachname ->  "Benz" */
    cy.get('[data-cy="input-lastname"]').should('exist').type('Benz');

    /* 5 - Geburtsdatum -> "25.03.1946" */
    cy.get('[data-cy="input-dayofbirth"]').should('exist').type('25.03.1946');

    /* 6 - Telefonnummer -> "062186319" */
    cy.get('[data-cy="input-phone"]').should('exist').type('062186319');

    /* 7 - Email -> "bbenz@mail.de" */
    cy.get('[data-cy="input-email"]').should('exist').type('bbenz@mail.de');

    /* 8 - Straße -> "Waldweg" */
    cy.get('[data-cy="street-input"]').should('exist').type('Waldweg');

    /* 9 - Hausnummer -> "2" */
    cy.get('[data-cy="house-number-input"]').should('exist').type('2');

    /* 10 - PLZ von Mannheim -> "68167" */
    cy.get('[data-cy="zip-code-input"]').should('exist').type('68167');

    /* 11 - Stadt -> "Mannheim" */
    cy.get('[data-cy="city-input"]').should('exist').type('Mannheim');

    /* 12 - wähle "Speichern und schließen" */
    cy.get('[data-cy="client-submit-and-close-button"] button').should('exist').click();

    /* CHECK: In Übersicht "Indexfälle" steht für "Berta Benz" der Status "angelegt" */
    cy.get('[data-cy="search-index-case-input"]').should('exist').type('Berta Benz');
    cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('angelegt');

    /* 13 - wähle Indexfall "Berta Benz" aus */
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .should('have.length.greaterThan', 0);
    cy.get('[data-cy="case-data-table"]')
      .find('.ag-center-cols-container > .ag-row')
      .then(($elems) => {
        $elems[0].click();
      });

    /* CHECK: Überprüfung, ob die Seite gewechselt wurde */
    //REGEX hinzufügen
    //const indexUrlReg = /^health-department\/case-detail\/index\/.*\/edit$/;
    //cy.url().should('match', /^health-department\/case-detail\/index\/.*\/edit$/);
    cy.url().should('contain', 'health-department/case-detail/index/');

    /* 14 - wähle "Nachverfolgung Starten" */
    cy.get('[data-cy="start-tracking-button"]').should('exist');
    cy.get('[data-cy="start-tracking-button"]').should('be.enabled');
    cy.get('[data-cy="start-tracking-button"]').click();

    /* CHECK: In Übersicht "Indexfälle" steht für "Berta Benz" der Status "in Registrierung" */
    cy.get('[data-cy="case-status"]').contains('in Registrierung');

    /* CHECK: Tab "Email-Vorlage" ist vorhanden */
    cy.get('.mat-tab-links').children().should('have.length', 6);
    cy.get('.mat-tab-links').children().contains('E-Mail Vorlage');

    /* 14A - Aufrufen der E-Mail Vorlage */
    cy.get('[data-cy="email-tab"]').should('exist').click();
    cy.wait(500);

    /* CHECK: Button "Aktivierungscode erneuern" ist vorhanden */
    cy.get('[data-cy="new-activation-code"]').should('exist');

    cy.get('[data-cy="mail-text"]')
      .should('exist')
      .then((elem) => {
        /* 15 - Extrahiere Anmeldelink aus dem Template */
        const extractedActivationCode = extractActivationCode(elem);

        /* 16 - Logout als GAMA */
        cy.logOut();

        /* CHECK: Logout war erfolgreich */
        cy.get('[data-cy="profile-user-button"]').should('not.exist');

        /* 17 - Anmeldelink aufrufen */
        cy.visit('/client/enrollment/landing/index/' + extractedActivationCode);

        /* 18 - Klick auf Weiter */
        cy.get('[data-cy="cta-button-index"]').click();
        cy.get('[data-cy="registration-submit-button"] button').should('be.disabled');

        /* 19 - Benutzername: "Berta" */
        cy.get('[data-cy="input-username"] input[matInput]').type('Berta');

        /* 20 - Passwort: "Password03!" */
        cy.get('[data-cy="input-password"] input[matInput]').type('Password03!');

        /* 21 - Passwort bestätgen  "Password03!" */
        cy.get('[data-cy="input-password-confirm"] input[matInput]').type('Password03!');

        /* 22 - Geburtsdatum: "25.03.1946" */
        cy.get('[data-cy="input-dateofbirth"] input[matInput]').type('25.03.1946');

        /* 23 - AGB aktivieren */
        cy.get('[data-cy="input-privacy-policy"]').click();

        /* 24 - Klick auf "Registrieren" Button */
        cy.get('[data-cy="registration-submit-button"] button').should('be.enabled');
        cy.get('[data-cy="registration-submit-button"] button').click();

        /* 25 - Klick auf "weiter" */
        cy.get('[data-cy="first-step-button"] button').click();

        /* 26 - Logout als Bürger */
        cy.logOut();

        /* 27 -  Login als Gama "agent1" */
        cy.logInAgent();

        /* CHECK:  In Übersicht "Indexfälle" steht für "Berta Benz" der Status "Registrierung abgeschlossen" */
        cy.get('[data-cy="search-index-case-input"]').should('exist').type('Berta Benz');
        cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('Registrierung abgeschlossen');

        /* 28 - Logout als GAMA */
        cy.logOut();

        /* 29 - Login als Bürger ("Berta"; "Password03!") */
        cy.logIn('Berta', 'Password03!');

        /* Check */
        cy.get('[data-cy="second-step-button"] button').should('be.disabled');

        /* 30 - Initialer Fragebogen "Covid-19-Symptome" -> "nein" */
        cy.get('[data-cy="has-no-symptoms-option"]').click();

        /* 31 - Bitte geben Sie Ihren behandelnden Hausarzt an. -> Dr. Schmidt */
        cy.get('[data-cy="familyDoctor"]').type('Dr. Schmidt');

        //TODO -> data-cy zu Komponente hinzufügen
        /* 32 - Nennen Sie uns bitte den (vermuteten) Ort der Ansteckung: -> "Familie" */
        cy.get('[formcontrolname="guessedOriginOfInfection"]').type('Familie');

        /* 33 - Haben Sie eine oder mehrere relevante Vorerkrankungen? -> "nein" */
        cy.get('[data-cy="has-no-pre-existing-conditions-option"]').click();

        /* 34 - Arbeiten Sie im medizinischen Umfeld oder in der Pflege? -> "nein" */
        cy.get('[data-cy="no-medical-staff-option"]').click();

        /* 35 - Haben Sie Kontakt zu Risikopersonen? -> "nein" */
        cy.get('[data-cy="no-contact-option"]').click();

        /* 36 - Klick "weiter" */
        cy.get('[data-cy="second-step-button"] button').should('be.enabled');
        cy.get('[data-cy="second-step-button"] button').click();

        /* CHECK: Weiter auf Seite 3 */
        cy.get('[data-cy="third-step-button"]').should('exist');

        //TODO
        cy.wait(500);

        /* 37 - Kontakte mit anderen Menschen -> "Carl Benz" */
        /* 38 - Klick enter */
        cy.get('[data-cy="multiple-auto-complete-input"]').should('exist').first().type('Carl Benz').blur();

        /* 39 - wähle "Kontakt anlegen" in Popup */
        cy.get('[data-cy="confirm-button"]').should('exist').click();

        //TODO
        cy.wait(500);

        /* 40 - Telefonnummer (mobil) -> "017196347526" */
        cy.get('[data-cy="input-mobilePhone"]').should('exist').type('017196347526');

        /* 41 - Klick auf "speichern" */
        cy.get('[data-cy="button-save"]').should('exist').click();

        /* 42 - Klick auf "Erfassung abschließen" */
        cy.get('[data-cy="third-step-button"]').should('exist').click();

        /* 43 - Logout als Bürger */
        cy.logOut();

        /* 44 - Login als GAMA "agent1" */
        cy.logInAgent();

        /* --> CHECK: In Übersicht "Indexfälle" steht für "Berta Benz" der Status "in Nachverfolgung" */
        cy.get('[data-cy="search-index-case-input"]').should('exist').type('Berta Benz');
        cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('Nachverfolgung');

        /* 45 - wähle Indexfall "Berta Benz" aus */
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .should('have.length.greaterThan', 0);
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .then(($elems) => {
            $elems[0].click();
          });

        /* 46 - wähle "Fall abschließen" */
        cy.get('[data-cy="button-closeCase"]').should('exist').click();

        //TODO
        cy.wait(500);

        /* 47 - Popup "Diesen Fall abschließen" geht auf */
        /* 48 - Zusätzliche Informationen zum Fallabschluss: -> "Quarantäne beendet" */
        cy.get('[data-cy="textarea-comment"]').should('exist').type('Quarantäne beendet');

        /* 49 - Klicke "OK" */
        cy.get('[data-cy="button-confirm"]').should('exist').click();

        /* 49A - wähle "Speichern und schließen" */
        cy.get('[data-cy="client-submit-and-close-button"] button').should('exist').click();

        /* 50 - wähle in Übersicht der Indexfälle den Filter "abgeschlossen" */
        cy.get('.ag-icon-menu').should('exist').first().click();
        cy.get('[data-cy="button-selectAll"]').should('exist').click();
        cy.get('[data-cy="button-unselectAll"]').should('exist').click();
        cy.get('[data-cy="checkbox-filter"]').should('exist').first().click();

        /* --> CHECK: In Übersicht "Indexfälle" steht für "Berta Benz" der Status "abgeschlossen" */
        cy.get('[data-cy="search-index-case-input"]').should('exist').type('Berta Benz');
        cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('abgeschlossen');

        /* 51 - Logout als GAMA */
        cy.logOut();
      });
  });
});
