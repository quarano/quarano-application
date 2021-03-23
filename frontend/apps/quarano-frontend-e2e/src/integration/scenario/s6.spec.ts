/// <reference types="cypress" />

import * as dayjs from 'dayjs';
import 'dayjs/locale/de';
import * as localeData from 'dayjs/plugin/localeData';

dayjs.locale('de');
dayjs.extend(localeData);

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
//    - Hausarzt: Dr. Maier - Bergstraße 34, Mannheim
//    - keine Symptome festgestellt
//    - keine Vorerkrankungen
//    - nicht mi
//    - kein Kontakt zu Risikopersonen
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
    });

    describe('enroll as client', () => {
      it('add new contact for index', () => {
        /* 1 - Login als Bürger Thorsten Mehler Login: test8:test123 */
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
        /* 7 - Nachname: -> "Hügel" */
        /* 8 - Telefonnummer: -> "0621486375" */
        /* 9 - Mail-Adresse: -> "chuegel@web.de" */
        /* 10 - Straße: -> "Sonderweg" */
        /* 11 - Hausnummer: -> "429" */
        /* 12 - Postleitzahl: -> "68239" */
        /* 13 - Ort: -> "Mannheim"14 - Risikoeinschätzung: arbeitet im medizinischen Umfeld: -> "ja" */
        /* 15 - Risikoeinschätzung: ist über 60: -> "ja" */
        /* 16 - Risikoeinschätzung: bekannt Vorerkrankungen: -> "nein" */
        /* CHECK: Bürger sieht alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) unter der Ansicht "Kontaktpersonen" */
        /* 17 - Logout als Bürger */
      });

      it('test for health department user', () => {
        /* 18 - Login als GAMA "agent1" */
        /* CHECK: Unter dem Reiter "Kontaktpersonen" sind alle Kontakte (Jack Randel, Claire Fraser, Roger Fraser und Conny Hügel) für GAMA sichtbar und mit Status "angelegt" erfasst */
        /* CHECK: für alle Kontakte (Leon Duerr, Anna Beike und Conny Hügel) ist der Ursprungsfall "Markus Hanser" in der Übersicht hinterlegt */
        /* 19 - suche unter dem Reiter "Indexfälle" "Markus Hanser" */
        /* CHECK: Unter "Kontakte" sind alle alten und neu angelegten Kontakte (Leon Duerr, Anna Beike und Conny Hügel) mit der dazugehörigen Risikoeinstufung  (für Conny Hügel erfasst) sichtbar */
        /* 20 - Logout als GAMA */
      });
    });
  }
);
