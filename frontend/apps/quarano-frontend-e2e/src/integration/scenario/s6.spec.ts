/// <reference types="cypress" />

/* Vorbedingung 1: GAMA hat Indexfall "Markus Hanser” mit Minimaldaten angelegt (S2) */
// Es wurde ein neuer User für den Test angelegt
// UUID: caaf631e-c7ba-495f-9a54-77e90a71100f
// Vorname: Thorsten
// Nachname: Mehler
// Geburtsdatum: 8.5.1983
// Festnetznummer: 0621703221
// E-Mail: ThorstenMehler@testtest.de
// Straße: Blütenweg 24
// Ort: 68199 Mannheim
// Initialer Fragebogen:
//    - Hausarzt: Prof. Dr. Maier - Bergstraße 34, Mannheim
//    - keine Symptome festgestellt
//    - keine Vorerkrankungen
//    - Origin of Infection: Schnitzel Wettessen in der Wirtschaft Zum blauen Schwert
//    - Kontakt zu Risikopersonen: Alterheim Ruhetstill
// siehe: backend/src/main/java/quarano/tracking/TrackedPersonDataInitializer.java

/* Vorbedingung 2: Bürger hat sich registriert, die Initiale Datenerfassung abgeschlossen und Kontakte sind angelegt (Claire Fraser, Peter Aalen) (S8) */
// Initiale Datenerfassung siehe Vorbedingung 1
// Kontakte werden angelegt
//  - Leon Duerr, leond@test.de
//  - Anna Beike, annabeike@mail.de
// siehe: backend/src/main/java/quarano/diary/DiaryDataInitializer.java

/* Vorbedingung 3: Tag +1 nach Registrierung, initialer Datenerfassung und Kontaktanlage */
// Account registrieren
// siehe: backend/src/main/java/quarano/department/RegistrationDataInitializer.java
// Setze Test Datum und Quarantäne Datum
// siehe: backend/src/main/java/quarano/department/TrackedCaseDataInitializer.java

describe(
  'S6 - Erfasste Kontakte in Indexfällen werden als Kontaktfälle angelegt und korrekt verknüpft',
  {
    defaultCommandTimeout: 20000,
    viewportWidth: 1300,
    viewportHeight: 1200,
  },
  () => {
    before((done) => {
      cy.restartBackend(done);
    });

    beforeEach(() => {
      cy.intercept('POST', '/diary').as('postDiary');
      cy.intercept({
        method: 'GET',
        url: /.*\/enrollment$/,
      }).as('getEnrollment');
      cy.intercept({
        method: 'GET',
        url: /.*\/contacts$/,
      }).as('getContacts');
      cy.intercept({
        method: 'POST',
        url: /.*\/contacts$/,
      }).as('postContacts');
      cy.intercept({
        method: 'GET',
        url: /.*\/hd\/cases\/[0-9A-Fa-f]{8}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{12}$/,
      }).as('getCaseDetails');
    });

    describe('enroll as client', () => {
      const contacts = ['Leon Duerr', 'Anna Beike', 'Conny Hügel'];

      it('add new contact for index', () => {
        /* 1 - Login als Bürger Thorsten Mehler (“test8”, “test123”) */
        cy.logIn('test8', 'test123');

        cy.wait('@getEnrollment').its('response.statusCode').should('eq', 200);

        /* 2 - Tagebuchansicht aufrufen */
        cy.get('[data-cy="diary-menu-item"]').should('exist').click();

        /* 2a - Wähle Eintrag hinzufügen */
        cy.get('[data-cy="add-diary-entry"]').should('exist').click();

        cy.wait('@getEnrollment').its('response.statusCode').should('eq', 200);
        cy.wait('@getContacts').its('response.statusCode').should('eq', 200);

        /* CHECK: Überprüfung, ob die Seite gewechselt wurde */
        //cy.url().should('contain', /.*\/client\/diary\/diary-detail\/new\/.*/);

        /* 3 - Körpertemperatur angeben -> 35°C */
        cy.get('[data-cy="body-temperature"]').should('exist').click(); //Slider

        /* 4 - "Kontakte mit anderen Menschen  seit dem letzten Eintrag": Eingabe -> "Leon Duerr" */
        cy.get('[data-cy="multiple-auto-complete-input"]').eq(1).should('exist').click().type('Leon Duerr');
        cy.get('.mat-option').should('exist').click();

        /* 5 - auswählen "Kontaktperson fehlt in der Liste" */
        cy.get('[data-cy="add-missing-contacts"]').should('exist').click();

        /* 6 - Vorname: -> "Conny" */
        cy.get('[data-cy="contact-person-form-firstName"]').should('exist').type('Conny');

        /* 7 - Nachname: -> "Hügel" */
        cy.get('[data-cy="contact-person-form-lastName"]').should('exist').type('Hügel');

        /* 8 - Telefonnummer: -> "0621486375" */
        cy.get('[data-cy="contact-person-form-phone"]').should('exist').type('0621486375');

        /* 9 - Mail-Adresse: -> "chuegel@web.de" */
        cy.get('[data-cy="contact-person-form-email"]').should('exist').type('chuegel@web.de');

        /* 10 - Straße: -> "Sonderweg" */
        cy.get('[data-cy="contact-person-form-street"]').should('exist').type('Sonderweg');

        /* 11 - Hausnummer: -> "429" */
        cy.get('[data-cy="contact-person-form-houseNumber"]').should('exist').type('429');

        /* 12 - Postleitzahl: -> "68239" */
        cy.get('[data-cy="contact-person-form-zipCode"]').should('exist').type('68239');

        /* 13 - Ort: -> "Mannheim" */
        cy.get('[data-cy="contact-person-form-city"]').should('exist').type('Mannheim');

        /* 14 - Risikoeinschätzung: arbeitet im medizinischen Umfeld: -> "ja" */
        cy.get('[data-cy="contact-person-form-isHealthStaff-true"]').should('exist').click();

        /* 15 - Risikoeinschätzung: ist über 60: -> "ja" */
        cy.get('[data-cy="contact-person-form-isSenior-true"]').should('exist').click();

        /* 16 - Risikoeinschätzung: bekannt Vorerkrankungen: -> "nein" */
        cy.get('[data-cy="contact-person-form-hasPreExistingConditions-false"]').should('exist').click();

        /* 16b - Klicke Speichern */
        cy.get('[data-cy="submit-button"]').should('exist').click();
        cy.wait('@postContacts').its('response.statusCode').should('eq', 201);

        /* 16c - Klicke Speichern */
        cy.get('[data-cy="save-diary-entry"]').should('exist').click();
        cy.wait('@postDiary').its('response.statusCode').should('eq', 201);

        /* CHECK: Bürger sieht alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) unter der Ansicht "Kontaktpersonen" */
        cy.get('[data-cy="contact-person-menu-item"]').should('exist').click();
        cy.wait('@getContacts').its('response.statusCode').should('eq', 200);

        cy.get('[data-cy="contact-person-list-name"]')
          .should('exist')
          .each(($el) => {
            expect($el.text()).to.be.oneOf(contacts);
          });

        /* 17 - Logout als Bürger */
        cy.logOut();
      });

      it('test for health department user', () => {
        /* 18 - Login als GAMA "agent1" */
        cy.logInAgent();

        /* 18a - Reiter Kontaktpersonen auswählen */
        cy.get('[data-cy="contact-cases"]').should('exist').click();

        /* CHECK richtige URL aufgerufen */
        cy.url().should('match', /.*\/health-department\/contact-cases\/case-list$/);

        /* CHECKS pro angelegtem Kontaktfall */
        for (let index = 0; index < contacts.length; index++) {
          /* CHECK: Unter dem Reiter "Kontaktpersonen" sind alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) für GAMA sichtbar und mit Status "angelegt" erfasst */
          cy.get('[data-cy="search-contact-case-input"]').should('exist').type(contacts[index]);
          cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('angelegt');

          /* CHECK: für alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) ist der Ursprungsfall "Thorsten Mehler" in der Übersicht hinterlegt */
          /* Auswählen der Kontaktperson */
          cy.get('[data-cy="case-data-table"]')
            .find('.ag-center-cols-container > .ag-row')
            .should('have.length.greaterThan', 0)
            .then(($elems) => {
              $elems[0].click();
            });

          /* CHECK richtige URL aufgerufen */
          cy.url().should(
            'match',
            /.*\/health-department\/case-detail\/contact\/[0-9A-Fa-f]{8}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{12}\/edit$/
          );

          cy.get('[data-cy="origin-case-element"]')
            .find('.mat-chip')
            .should('have.length.greaterThan', 0)
            .then(($elems) => {
              const text = $elems.text();
              expect(text).to.contain('Mehler, Thorsten');
            });

          cy.get('[data-cy="contact-cases"]').should('exist').click();

          /* CHECK richtige URL aufgerufen */
          cy.url().should('match', /.*\/health-department\/contact-cases\/case-list$/);
        }

        /* 19 - Öffne den Reiter "Indexfälle" */
        cy.get('[data-cy="index-cases"]').should('exist').click();

        /* CHECK richtige URL aufgerufen */
        cy.url().should('match', /.*\/health-department\/index-cases\/case-list$/);

        /* 19a - suche unter dem Reiter "Indexfälle" "Thorsten Mehler" */
        cy.get('[data-cy="search-index-case-input"]').should('exist').type('Thorsten Mehler');
        cy.get('[data-cy="case-data-table"]')
          .find('.ag-center-cols-container > .ag-row')
          .should('have.length.greaterThan', 0)
          .then(($elems) => {
            $elems[0].click();
          });

        /* 19b - öffne den Tab "Kontakte" */
        cy.get('[data-cy="contacts-tab"]').should('exist').click();

        /* CHECK richtige URL aufgerufen */
        cy.url().should(
          'match',
          /.*\/health-department\/case-detail\/index\/[0-9A-Fa-f]{8}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{4}[-][0-9A-Fa-f]{12}\/contacts$/
        );

        /* CHECK: Unter "Kontakte" sind alle alten und neu angelegten Kontakte (Leon Duerr, Anna Beike und Conny Hügel) mit der dazugehörigen Risikoeinstufung  (für Conny Hügel erfasst) sichtbar */
        checkContactExists('Leon', 'Duerr');
        checkContactExists('Anna', 'Beike');
        checkContactExists('Conny', 'Hügel');

        /* 20 - Logout als GAMA */
        cy.logOut();

        function checkContactExists(firstName: string, lastName: string) {
          cy.get('[col-id="lastName"] > .ag-cell-label-container > .ag-header-cell-menu-button > .ag-icon')
            .should('exist')
            .click();

          cy.get('.ag-filter-filter').first().should('exist').clear().type(lastName);

          cy.get('[col-id="firstName"] > .ag-cell-label-container > .ag-header-cell-menu-button > .ag-icon')
            .should('exist')
            .click();

          cy.get('.ag-filter-filter').first().should('exist').clear().type(firstName);

          cy.get('.ag-center-cols-container > div > [col-id="lastName"]').contains(lastName);
          cy.get('.ag-center-cols-container > div > [col-id="firstName"]').contains(firstName);
          cy.get('.ag-center-cols-container > div > [col-id="status"]').contains('angelegt');
        }
      });
    });
  }
);
