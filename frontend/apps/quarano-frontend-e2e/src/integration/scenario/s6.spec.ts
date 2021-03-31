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
  },
  () => {
    before((done) => {
      cy.restartBackend(done);
    });

    beforeEach(() => {
      cy.intercept('GET', '/enrollment').as('getEnrollment');
      cy.intercept('POST', '/contacts').as('postContacts');
      cy.intercept('GET', '/contacts').as('getContacts');
      cy.intercept('POST', '/diary').as('postDiary');
    });

    describe('enroll as client', () => {
      it('add new contact for index', () => {
        /* 1 - Login als Bürger Thorsten Mehler (“test8”, “test123”) */
        cy.logIn('test8', 'test123');

        cy.wait('@getEnrollment').its('response.statusCode').should('eq', 200);

        /* 2 - Tagebuchansicht aufrufen */
        cy.get('[data-cy="diary-menu-item"]').should('exist').click();

        /* 2a - Wähle Eintrag hinzufügen */
        cy.get('[data-cy="add-diary-entry"]').should('exist').click();

        /* 3 - Körpertemperatur angeben -> 38,1°C */
        cy.get('[data-cy="body-temperature"]').should('exist').click(); //Slider

        /* 4 - "Kontakte mit anderen Menschen  seit dem letzten Eintrag": Eingabe -> "Leon Duerr" */
        cy.get('[data-cy="multiple-auto-complete-input"]').eq(1).should('exist').click().type('Leon Duerr');

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
            expect($el.text()).to.be.oneOf(['Leon Duerr', 'Anna Beike', 'Conny Hügel']);
          });

        /* 17 - Logout als Bürger */
        cy.logOut();
      });

      it('test for health department user', () => {
        /* 18 - Login als GAMA "agent1" */
        /* CHECK: Unter dem Reiter "Kontaktpersonen" sind alle Kontakte (Claire Fraser, Roger Fraser und Conny Hügel) für GAMA sichtbar und mit Status "angelegt" erfasst */
        /* CHECK: für alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) ist der Ursprungsfall "Markus Hanser" in der Übersicht hinterlegt */
        /* 19 - suche unter dem Reiter "Indexfälle" "Markus Hanser" */
        /* CHECK: Unter "Kontakte" sind alle alten und neu angelegten Kontakte (Leon Duerr, Anna Beike und Conny Hügel) mit der dazugehörigen Risikoeinstufung  (für Conny Hügel erfasst) sichtbar */
        /* 20 - Logout als GAMA */
      });
    });
  }
);
