import { TranslateService } from '@ngx-translate/core';
import { SubSink } from 'subsink';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContactPersonDto } from '@qro/client/domain';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'qro-contact-persons',
  templateUrl: './contact-persons.component.html',
  styleUrls: ['./contact-persons.component.scss'],
})
export class ContactPersonsComponent implements OnInit {
  contacts: ContactPersonDto[] = [];
  private subs = new SubSink();

  constructor(private route: ActivatedRoute, private translate: TranslateService) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.contacts = data.contacts;
      })
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
