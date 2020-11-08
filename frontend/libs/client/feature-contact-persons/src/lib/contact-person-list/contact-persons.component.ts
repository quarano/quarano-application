import { TranslateService } from '@ngx-translate/core';
import { SubSink } from 'subsink';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContactPersonDto, ContactPersonService } from '@qro/client/domain';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ForgottenContactDialogComponent } from '@qro/client/ui-forgotten-contact-dialog';

@Component({
  selector: 'qro-contact-persons',
  templateUrl: './contact-persons.component.html',
  styleUrls: ['./contact-persons.component.scss'],
})
export class ContactPersonsComponent implements OnInit {
  contacts: ContactPersonDto[] = [];
  private subs = new SubSink();

  constructor(
    private route: ActivatedRoute,
    private translate: TranslateService,
    private contactPersonService: ContactPersonService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.contacts = data.contacts;
      })
    );
  }

  getAlertMessage(): Observable<string> {
    return this.translate
      .get(['CONTACT_PERSONS.HIER_KÖNNEN_SIE_PERSONEN_ERFASSEN', 'CONTACT_PERSONS.KLICKEN_SIE_HIER'])
      .pipe(
        map((translations) => {
          let result = translations['CONTACT_PERSONS.HIER_KÖNNEN_SIE_PERSONEN_ERFASSEN'];
          result += ', <a href="#" (click)="!!openContactDialog()">';
          result += translations['CONTACT_PERSONS.KLICKEN_SIE_HIER'];
          result += '</a>.';
          return result;
        })
      );
  }

  openContactDialog() {
    this.subs.add(
      this.contactPersonService.getContactPersons().subscribe((contactPersons) =>
        this.dialog.open(ForgottenContactDialogComponent, {
          disableClose: true,
          data: {
            contactPersons,
          },
        })
      )
    );
  }

  getName(contact: ContactPersonDto): Observable<string> {
    let name: string;
    if (contact.firstName && contact.lastName) {
      name = `${contact.firstName} ${contact.lastName}`;
    } else if (contact.firstName) {
      name = contact.firstName;
    } else if (contact.lastName) {
      name = contact.lastName;
    }
    if (name) {
      return of(name);
    }
    return this.translate.get('CONTACT_PERSONS.ANONYME_KONTAKTPERSON');
  }
}
