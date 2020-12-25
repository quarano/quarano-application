/// <reference types="cypress" />

describe('new case', () => {
  beforeEach(() => {
    cy.server();
    cy.route('POST', '/hd/cases/?type=index').as('createCase');
    cy.route('GET', '/hd/cases/').as('getCases');
    cy.route('GET', '/user/me').as('me');

    cy.loginAgent();
  });

  describe('index', () => {
    it('valid form (with phone)', () => {
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');

      cy.wait('@me').its('status').should('eq', 200);
      cy.wait('@getCases').its('status').should('eq', 200);

      cy.get('[data-cy="new-case-button"]').click();

      cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.disabled');

      cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
      cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
      cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');

      cy.get('[data-cy="client-submit-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').should('be.enabled');
      cy.get('[data-cy="client-submit-and-close-button"] button').click();
      cy.location('pathname').should('eq', '/health-department/index-cases/case-list');
      cy.wait('@createCase').its('status').should('eq', 201);

      const today = new Date();
      const todayString = today.toISOString().split('T')[0];
      today.setDate(today.getDate() + 14);
      const twoWeeksFromNowString = today.toISOString().split('T')[0];

      cy.get('@createCase')
        .its('response.body')
        .then((body) => {
          console.dir(body);
          expect(body.caseId).not.to.eq(null);
          expect(body.caseId).not.to.eq('');
          expect(body.caseType).to.eq('index');
          expect(body.caseTypeLabel).to.eq('Index');
          expect(body.city).to.eq(null);
          expect(body.comments).to.be.an('array').that.does.have.length(0);
          expect(body.contactCount).to.eq(0);
          expect(body.createdAt).to.eq(todayString);
          expect(body.dateOfBirth).to.eq(null);
          expect(body.email).to.eq(null);
          expect(body.extReferenceNumber).to.eq(null);
          expect(body.firstName).to.eq('Testfirstname');
          expect(body.houseNumber).to.eq(null);
          expect(body.indexContacts).to.be.an('array').that.does.have.length(0);
          expect(body.infected).to.be.eq(true);
          expect(body.lastName).to.eq('Testlastname');
          expect(body.locale).to.eq(null);
          expect(body.mobilePhone).to.eq(null);
          expect(body.openAnomaliesCount).to.eq(1);
          expect(body.phone).to.eq('06212391201');
          expect(body.quarantineEndDate).to.eq(twoWeeksFromNowString);
          expect(body.quarantineStartDate).to.eq(todayString);
          expect(body.status).to.eq('angelegt');
          expect(body.street).to.eq(null);
          expect(body.testDate).to.eq(todayString);
          expect(body.zipCode).to.eq(null);
        });
    });

    // TODO: unit tests
    // describe('disabled submit button', () => {
    //   it('empty form', () => {
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing firstname', () => {
    //     cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    //
    //     cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing lastname', () => {
    //     cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');
    //
    //     cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing phone or mobile', () => {
    //     cy.get('[data-cy="input-firstname"] input[matInput]').type('Testlastname');
    //     cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    //
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing test date', () => {
    //     cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
    //     cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    //     cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
    //
    //     cy.get('[data-cy="input-testdate"]').should('exist');
    //     cy.get('[data-cy="input-testdate"] input[matInput]').clear();
    //
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing quarantine start date', () => {
    //     cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
    //     cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    //     cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
    //
    //     cy.get('[data-cy="input-quarantinestart"]').should('exist');
    //     cy.get('[data-cy="input-quarantinestart"] input[matInput]').clear();
    //
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    //
    //   it('missing quarantine end date', () => {
    //     cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
    //     cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
    //     cy.get('[data-cy="input-phone"] input[matInput]').type('0621 239 120 1');
    //
    //     cy.get('[data-cy="input-quarantineend"]').should('exist');
    //     cy.get('[data-cy="input-quarantineend"] input[matInput]').clear();
    //
    //     cy.get('[data-cy="client-submit-button"] button').should('be.disabled');
    //   });
    // });
  });

  // describe('field tests', () => {
  //   const dateChecks = (selector: string, calendarClass: string, dateInput: string) => {
  //     it('should be a date string', () => {
  //       cy.get(selector).should('exist');
  //       cy.get(selector + ' input[matInput]')
  //         .clear()
  //         .type('invalid');
  //       cy.get(selector + ' input[matInput]').blur();
  //
  //       cy.get(selector + ' mat-error').should('contain.text', 'Bitte füllen Sie dieses Pflichtfeld aus');
  //     });
  //
  //     it('should be valid a correct date string', () => {
  //       cy.get(selector).should('exist');
  //       cy.get(selector + ' input[matInput]')
  //         .clear()
  //         .type(dateInput);
  //       cy.get(selector + ' input[matInput]').blur();
  //
  //       cy.get(selector + ' mat-error').should('not.exist');
  //     });
  //
  //     it('should open the date picker and click valid date', () => {
  //       cy.get(selector).should('exist');
  //
  //       cy.get(selector + ' mat-datepicker-toggle').click();
  //       cy.get('mat-datepicker-content').should('exist');
  //       cy.get('mat-datepicker-content ' + calendarClass).click();
  //       cy.get(selector + ' mat-error').should('not.exist');
  //     });
  //   };

  // describe('base data', () => {
  //   describe('firstName', () => {
  //     it('should not be empty', () => {
  //       cy.get('[data-cy="input-firstname"] input[matInput]').type('Testfirstname');
  //       cy.get('[data-cy="input-firstname"] input[matInput]').clear();
  //       cy.get('[data-cy="input-firstname"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-firstname"] mat-error').should(
  //         'contain.text',
  //         'Bitte füllen Sie dieses Pflichtfeld aus'
  //       );
  //     });
  //     it('should be invalid', () => {
  //       cy.get('[data-cy="input-firstname"] input[matInput]').clear().type('Testfirstn5ame');
  //       cy.get('[data-cy="input-firstname"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-firstname"] mat-error').should(
  //         'contain.text',
  //         'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
  //       );
  //     });
  //   });
  //
  //   describe('lastName', () => {
  //     it('should not be empty', () => {
  //       cy.get('[data-cy="input-lastname"] input[matInput]').type('Testlastname');
  //       cy.get('[data-cy="input-lastname"] input[matInput]').clear();
  //       cy.get('[data-cy="input-lastname"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-lastname"] mat-error').should(
  //         'contain.text',
  //         'Bitte füllen Sie dieses Pflichtfeld aus'
  //       );
  //     });
  //     it('should be invalid', () => {
  //       cy.get('[data-cy="input-lastname"] input[matInput]').clear().type('Testlast09name');
  //       cy.get('[data-cy="input-lastname"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-lastname"] mat-error').should(
  //         'contain.text',
  //         'Bitte geben Sie einen gültigen Namen ein. Es sind nur Buchstaben, Leerzeichen und Bindestriche erlaubt'
  //       );
  //     });
  //   });
  //
  //   describe('dayOfBirth', () => {
  //     it('should be a date string', () => {
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('invalid');
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-dayofbirth"] mat-error').should(
  //         'contain.text',
  //         'Bitte geben Sie ein gültiges Datum ein'
  //       );
  //     });
  //
  //     it('should be a date string lower or equal today', () => {
  //       const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString('de');
  //
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').type(dateTomorrow);
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-dayofbirth"] mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
  //     });
  //
  //     it('should be valid a correct date string', () => {
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').type('20.12.1987');
  //       cy.get('[data-cy="input-dayofbirth"] input[matInput]').blur();
  //
  //       cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
  //     });
  //
  //     it('should open the date picker and click valid date', () => {
  //       cy.get('[data-cy="input-dayofbirth"] mat-datepicker-toggle').click();
  //       cy.get('mat-datepicker-content').should('exist');
  //       cy.get('mat-datepicker-content .mat-calendar-body-today').click();
  //       cy.get('[data-cy="input-dayofbirth"] mat-error').should('not.exist');
  //     });
  //   });
  // });
  //
  //   describe('disease data', () => {
  //     describe('test date', () => {
  //       dateChecks('[data-cy="input-testdate"]', '.mat-calendar-body-today', '04.06.2020');
  //
  //       it('should be a date string lower or equal today', () => {
  //         const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString('de');
  //         cy.get('[data-cy="input-testdate"]').should('exist');
  //         cy.get('[data-cy="input-testdate"] input[matInput]').clear().type(dateTomorrow);
  //         cy.get('[data-cy="input-testdate"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-testdate"] mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
  //       });
  //     });
  //
  //     describe('quarantine start date', () => {
  //       const selector = '[data-cy="input-quarantinestart"]';
  //       dateChecks(selector, '.mat-calendar-body-today', '04.06.2020');
  //
  //       it('should be a date string lower or equal today', () => {
  //         const dateTomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toLocaleDateString('de');
  //         cy.get(selector).should('exist');
  //         cy.get(selector + ' input[matInput]')
  //           .clear()
  //           .type(dateTomorrow);
  //         cy.get(selector + ' input[matInput]').blur();
  //
  //         cy.get(selector + ' mat-error').should('contain.text', 'Das größte zulässige Datum ist der');
  //       });
  //
  //       it('should set quarantine end date to startdate + 14 days', () => {
  //         const now = new Date();
  //         const fourteenDaysLater = new Date(new Date().setDate(now.getDate() + 14));
  //         cy.get(selector).should('exist');
  //         cy.get(selector + ' input[matInput]')
  //           .clear()
  //           .type(now.toLocaleDateString('de'));
  //         cy.get(selector + ' input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-quarantineend"] input[matInput]').should(
  //           'contain.value',
  //           fourteenDaysLater.toLocaleDateString('de')
  //         );
  //       });
  //     });
  //
  //     describe('quarantine end date', () => {
  //       const date = DateFunctions.toCustomLocaleDateString(DateFunctions.addDays(new Date(), 14));
  //       dateChecks('[data-cy="input-quarantineend"]', '.mat-calendar-body-selected', date);
  //     });
  //   });
  //
  //   describe('contact data', () => {
  //     describe('phone number', () => {
  //       it('should have a valid phone number', () => {
  //         cy.get('[data-cy="input-phone"] input[matInput]').type('0621 754302');
  //         cy.get('[data-cy="input-phone"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-phone"] mat-error').should('not.exist');
  //       });
  //
  //       it('should be invalid with too short phone number', () => {
  //         cy.get('[data-cy="input-phone"] input[matInput]').type('0621');
  //         cy.get('[data-cy="input-phone"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-phone"] mat-error').should(
  //           'contain.text',
  //           'Dieses Feld erfordert eine Eingabe von mindestens 5 Zeichen'
  //         );
  //       });
  //
  //       it('should be invalid with invalid symbols', () => {
  //         cy.get('[data-cy="input-phone"] input[matInput]').type('0621 i');
  //         cy.get('[data-cy="input-phone"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-phone"] mat-error').should(
  //           'contain.text',
  //           'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
  //         );
  //
  //         cy.get('[data-cy="input-phone"] input[matInput]').type('0621 $');
  //         cy.get('[data-cy="input-phone"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-phone"] mat-error').should(
  //           'contain.text',
  //           'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
  //         );
  //       });
  //
  //       it('should be shortened long phone number to 17 symbols', () => {
  //         cy.get('[data-cy="input-phone"] input[matInput]').type('0621 123456789 1234567');
  //         cy.get('[data-cy="input-phone"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-phone"] input[matInput]').should('contain.value', '0621 123456789 12');
  //       });
  //     });
  //
  //     describe('mobile phone number', () => {
  //       it('should have a valid phone number', () => {
  //         cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 754302');
  //         cy.get('[data-cy="input-mobile"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-mobile"] mat-error').should('not.exist');
  //       });
  //
  //       it('should be invalid with too short phone number', () => {
  //         cy.get('[data-cy="input-mobile"] input[matInput]').type('0621');
  //         cy.get('[data-cy="input-mobile"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-mobile"] mat-error').should(
  //           'contain.text',
  //           'Dieses Feld erfordert eine Eingabe von mindestens 5 Zeichen'
  //         );
  //       });
  //
  //       it('should be invalid with invalid symbols', () => {
  //         cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 i');
  //         cy.get('[data-cy="input-mobile"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-mobile"] mat-error').should(
  //           'contain.text',
  //           'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
  //         );
  //
  //         cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 $');
  //         cy.get('[data-cy="input-mobile"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-mobile"] mat-error').should(
  //           'contain.text',
  //           'Bitte geben Sie eine gültige Telefonnummer an! Diese kann Zahlen, +, -, Klammern oder Leerzeichen enthalten'
  //         );
  //       });
  //
  //       it('should be shortened long phone number to 17 symbols', () => {
  //         cy.get('[data-cy="input-mobile"] input[matInput]').type('0621 123456789 1234567');
  //         cy.get('[data-cy="input-mobile"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-mobile"] input[matInput]').should('contain.value', '0621 123456789 12');
  //       });
  //     });
  //     describe('email address', () => {
  //       it('should have a valid email address', () => {
  //         cy.get('[data-cy="input-email"] input[matInput]').type('test@test.de');
  //         cy.get('[data-cy="input-email"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-email"] mat-error').should('not.exist');
  //       });
  //
  //       it('should be invalid without correct email structure', () => {
  //         cy.get('[data-cy="input-email"] input[matInput]').type('te@st@test.de');
  //         cy.get('[data-cy="input-email"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-email"] mat-error').should('exist');
  //
  //         cy.get('[data-cy="input-email"] input[matInput]').clear().type('test.de');
  //         cy.get('[data-cy="input-email"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-email"] mat-error').should('exist');
  //
  //         cy.get('[data-cy="input-email"] input[matInput]').clear().type('test@testde');
  //         cy.get('[data-cy="input-email"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-email"] mat-error').should('exist');
  //
  //         cy.get('[data-cy="input-email"] input[matInput]').clear().type('testde');
  //         cy.get('[data-cy="input-email"] input[matInput]').blur();
  //
  //         cy.get('[data-cy="input-email"] mat-error').should('exist');
  //       });
  //     });
  //
  //     describe('address data', () => {
  //       describe('zip code', () => {
  //         it('should be valid', () => {
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').type('22041');
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
  //
  //           cy.get('[data-cy="input-mobile"] mat-error').should('not.exist');
  //         });
  //
  //         it('should be invalid with invalid characters', () => {
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').type('2204i');
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
  //
  //           cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
  //
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').clear().type('2204$');
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
  //
  //           cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
  //         });
  //
  //         it('should be invalid with too short zip', () => {
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').type('2204');
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
  //
  //           cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
  //         });
  //         it('should be invalid with too long zip', () => {
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').type('22041001');
  //           cy.get('[data-cy="input-zipcode"] input[matInput]').blur();
  //           cy.get('[data-cy="input-zipcode"] mat-error').should('exist');
  //         });
  //       });
  //     });
  //   });
  // });
});
