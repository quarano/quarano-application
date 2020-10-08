import { DateFunctions } from '@qro/shared/util-date';
/// <reference types="cypress" />

describe('new case', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/api/hd/cases' /*, 'fixture:get-api-hd-cases.json'*/).as('createcase');
    cy.route('PUT', '/api/hd/cases' /*, 'fixture:get-api-hd-cases.json'*/).as('updatecase');

    cy.loginAgent();

    cy.visit('health-department/case-detail/new/index/edit');
  });

  describe('field validations: required', () => {
    describe('enabled submit button', () => {
      it('valid form (with phone)', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

        cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      });

      it('valid form (with mobile)', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 239 120 1');

        cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      });
    });

    describe('disabled submit button', () => {
      it('empty form', () => {
        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing firstname', () => {
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');

        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing lastname', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');

        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing phone or mobile', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');

        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing test date', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

        cy.get('[data-cy="input-testdate"]').should('exist');
        cy.get('[data-cy="input-testdate"] input[matInput]').clear();

        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing quarantine start date', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

        cy.get('[data-cy="input-quarantinestart"]').should('exist');
        cy.get('[data-cy="input-quarantinestart"] input[matInput]').clear();

        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });

      it('missing quarantine end date', () => {
        cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
        cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
        cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

        cy.get('[data-cy="input-quarantineend"]').should('exist');
        cy.get('[data-cy="input-quarantineend"] input[matInput]').clear();

        cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      });
    });
  });

  describe('field tests', () => {
    const dateChecks = (selector: string, calendarClass: string, dateInput: string) => {
      it('should be a date string', () => {
        cy.get(selector).should('exist');
        cy.get(selector + ' input[matInput]')
          .clear()
          .type('invalid');
        cy.get(selector + ' input[matInput]').blur();

        cy.get(selector + ' mat-error').should('contain.text', 'Bitte füllen Sie dieses Pflichtfeld aus');
      });

      it('should be valid a correct date string', () => {
        cy.get(selector).should('exist');
        cy.get(selector + ' input[matInput]')
          .clear()
          .type(dateInput);
        cy.get(selector + ' input[matInput]').blur();

        cy.get(selector + ' mat-error').should('not.exist');
      });

      it('should open the date picker and click valid date', () => {
        cy.get(selector).should('exist');

        cy.get(selector + ' mat-datepicker-toggle').click();
        cy.get('mat-datepicker-content').should('exist');
        cy.get('mat-datepicker-content ' + calendarClass).click();
        cy.get(selector + ' mat-error').should('not.exist');
      });
    };

    describe('base data', () => {
      describe('firstName', () => {
        it('should not be empty', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
          cy.get('[data-cy="input-firstname"] input[matInput]').clear();
          cy.get('[data-cy="input-firstname"] input[matInput]').blur();

          cy.get('[data-cy="input-firstname"] mat-error').should(
            'contain.text',
            'Bitte füllen Sie dieses Pflichtfeld aus'
          );
        });
        it('should be invalid', () => {
          cy.get('[data-cy="input-firstname"] input[matInput]').clear().type('Testfirstn5ame');
          cy.get('[data-cy="input-firstname"] input[matInput]').blur();

          cy.get('[data-cy="input-firstname"] mat-error').should(
            'contain.text',
            'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
          );
        });
      });

      describe('lastName', () => {
        it('should not be empty', () => {
          cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
          cy.get('[data-cy="input-lastname"] input[matInput]').clear();
          cy.get('[data-cy="input-lastname"] input[matInput]').blur();

          cy.get('[data-cy="input-lastname"] mat-error').should(
            'contain.text',
            'Bitte füllen Sie dieses Pflichtfeld aus'
          );
        });
        it('should be invalid', () => {
          cy.get('[data-cy="input-lastname"] input[matInput]').clear().type('Testlast09name');
          cy.get('[data-cy="input-lastname"] input[matInput]').blur();

          cy.get('[data-cy="input-lastname"] mat-error').should(
            'contain.text',
            'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
          );
        });
      });

      describe('dayOfBirth', () => {
        it('should be a date string', () => {
          cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('invalid');
          cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

          cy.get('[data-cy="input-dayofbirth"] mat-error').should(
            'contain.text',
            'Bitte geben Sie ein gültiges Datum ein'
          );
        });

        it('should be a date string lower or equal today', () => {
          const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString();

          cy.get('[data-cy="input-dayofbirth"] input[matInput]').type(dateTomorrow);
          cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

          cy.get('[data-cy="input-dayofbirth"] mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
        });

        it('should be valid a correct date string', () => {
          cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('20.12.1987');
          cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();

          cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
        });

        it('should open the date picker and click valid date', () => {
          cy.get('[data-cy="input-dayofbirth"] mat-datepicker-toggle').click();
          cy.get('mat-datepicker-content').should('exist');
          cy.get('mat-datepicker-content .mat-calendar-body-today').click();
          cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
        });
      });
    });

    describe('disease data', () => {
      describe('test date', () => {
        dateChecks('[data-cy="input-testdate"]', '.mat-calendar-body-today', '04.06.2020');

        it('should be a date string lower or equal today', () => {
          const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString();
          cy.get('[data-cy="input-testdate"]').should('exist');
          cy.get('[data-cy="input-testdate"] input[matInput]').clear().type(dateTomorrow);
          cy.get('[data-cy="input-testdate"] input[matInput]').blur();

          cy.get('[data-cy="input-testdate"] mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
        });
      });

      describe('quarantine start date', () => {
        const selector = '[data-cy="input-quarantinestart"]';
        dateChecks(selector, '.mat-calendar-body-today', '04.06.2020');

        it('should be a date string lower or equal today', () => {
          const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString();
          cy.get(selector).should('exist');
          cy.get(selector + ' input[matInput]')
            .clear()
            .type(dateTomorrow);
          cy.get(selector + ' input[matInput]').blur();

          cy.get(selector + ' mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
        });

        it('should set quarantine end date to startdate + 14 days', () => {
          const now = new Date();
          const fourteenDaysLater = new Date(new Date().setDate(now.getDate() + 14));
          cy.get(selector).should('exist');
          cy.get(selector + ' input[matInput]')
            .clear()
            .type(now.toLocaleDateString());
          cy.get(selector + ' input[matInput]').blur();

          cy.get('[data-cy="input-quarantineend"] input[matInput]').should(
            'contain.value',
            fourteenDaysLater.toLocaleDateString()
          );
        });
      });

      describe('quarantine end date', () => {
        const date = DateFunctions.toCustomLocaleDateString(DateFunctions.addDays(new Date(), 14));
        dateChecks('[data-cy="input-quarantineend"]', '.mat-calendar-body-selected', date);
      });
    });

    describe('contact data', () => {
      describe('phone number', () => {
        it('should have a valid phone number', () => {
          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 754302');
          cy.get('[data-cy="input-phone"] input[matInput]').blur();

          cy.get('[data-cy="input-phone"] mat-error').should('not.exist');
        });

        it('should be invalid with too short phone number', () => {
          cy.get('[data-cy="input-phone"] input[matInput]').type('0621');
          cy.get('[data-cy="input-phone"] input[matInput]').blur();

          cy.get('[data-cy="input-phone"] mat-error').should(
            'contain.text',
            'Dieses Feld erfordert eine Eingabe von mindestens 5 Zeichen'
          );
        });

        it('should be invalid with invalid symbols', () => {
          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 i');
          cy.get('[data-cy="input-phone"] input[matInput]').blur();

          cy.get('[data-cy="input-phone"] mat-error').should(
            'contain.text',
            'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
          );

          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 $');
          cy.get('[data-cy="input-phone"] input[matInput]').blur();

          cy.get('[data-cy="input-phone"] mat-error').should(
            'contain.text',
            'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
          );
        });

        it('should be shortened long phone number to 17 symbols', () => {
          cy.get('[data-cy="input-phone"] input[matInput]').type('0621 123456789 1234567');
          cy.get('[data-cy="input-phone"] input[matInput]').blur();

          cy.get('[data-cy="input-phone"] input[matInput]').should('contain.value', '0621 123456789 12');
        });
      });

      describe('mobile phone number', () => {
        it('should have a valid phone number', () => {
          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 754302');
          cy.get('[data-cy="input-mobile"] input[matInput]').blur();

          cy.get('[data-cy="input-mobile"] mat-error').should('not.exist');
        });

        it('should be invalid with too short phone number', () => {
          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621');
          cy.get('[data-cy="input-mobile"] input[matInput]').blur();

          cy.get('[data-cy="input-mobile"] mat-error').should(
            'contain.text',
            'Dieses Feld erfordert eine Eingabe von mindestens 5 Zeichen'
          );
        });

        it('should be invalid with invalid symbols', () => {
          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 i');
          cy.get('[data-cy="input-mobile"] input[matInput]').blur();

          cy.get('[data-cy="input-mobile"] mat-error').should(
            'contain.text',
            'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
          );

          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 $');
          cy.get('[data-cy="input-mobile"] input[matInput]').blur();

          cy.get('[data-cy="input-mobile"] mat-error').should(
            'contain.text',
            'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
          );
        });

        it('should be shortened long phone number to 17 symbols', () => {
          cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 123456789 1234567');
          cy.get('[data-cy="input-mobile"] input[matInput]').blur();

          cy.get('[data-cy="input-mobile"] input[matInput]').should('contain.value', '0621 123456789 12');
        });
      });
      describe('email address', () => {
        it('should have a valid email address', () => {
          cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
          cy.get('[data-cy="input-email"] input[matInput]').blur();

          cy.get('[data-cy="input-email"] mat-error').should('not.exist');
        });

        it('should be invalid without correct email structure', () => {
          cy.get('[data-cy="input-email"] input[matInput]').type('te@st@test.de');
          cy.get('[data-cy="input-email"] input[matInput]').blur();

          cy.get('[data-cy="input-email"] mat-error').should('exist');

          cy.get('[data-cy="input-email"] input[matInput]').clear().type('test.de');
          cy.get('[data-cy="input-email"] input[matInput]').blur();

          cy.get('[data-cy="input-email"] mat-error').should('exist');

          cy.get('[data-cy="input-email"] input[matInput]').clear().type('test@testde');
          cy.get('[data-cy="input-email"] input[matInput]').blur();

          cy.get('[data-cy="input-email"] mat-error').should('exist');

          cy.get('[data-cy="input-email"] input[matInput]').clear().type('testde');
          cy.get('[data-cy="input-email"] input[matInput]').blur();

          cy.get('[data-cy="input-email"] mat-error').should('exist');
        });
      });

      describe('address data', () => {
        describe('zip code', () => {
          it('should be valid', () => {
            cy.get('[data-cy="input-zipcode"] input[matInput]').type('22041');
            cy.get('[data-cy="input-zipcode"] input[matInput]').blur();

            cy.get('[data-cy="input-mobile"] mat-error').should('not.exist');
          });

          it('should be invalid with invalid characters', () => {
            cy.get('[data-cy="input-zipcode"] input[matInput]').type('2204i');
            cy.get('[data-cy="input-zipcode"] input[matInput]').blur();

            cy.get('[data-cy="input-zipcode"] mat-error').should('exist');

            cy.get('[data-cy="input-zipcode"] input[matInput]').clear().type('2204$');
            cy.get('[data-cy="input-zipcode"] input[matInput]').blur();

            cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
          });

          it('should be invalid with too short zip', () => {
            cy.get('[data-cy="input-zipcode"] input[matInput]').type('2204');
            cy.get('[data-cy="input-zipcode"] input[matInput]').blur();

            cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
          });
          it('should be invalid with too long zip', () => {
            cy.get('[data-cy="input-zipcode"] input[matInput]').type('22041001');
            cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
            cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
          });
        });
      });
    });
  });
});
