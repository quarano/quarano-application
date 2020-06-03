import { SubSink } from 'subsink';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContactPersonDto } from '@qro/client/contact-persons/domain';

@Component({
  selector: 'qro-contact-persons',
  templateUrl: './contact-persons.component.html',
  styleUrls: ['./contact-persons.component.scss'],
})
export class ContactPersonsComponent implements OnInit {
  contacts: ContactPersonDto[] = [];
  private subs = new SubSink();

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.contacts = data.contacts;
      })
    );
  }

  getName(contact: ContactPersonDto): string {
    let name = 'anonymer Kontakt';
    if (contact.firstName && contact.lastName) {
      name = `${contact.firstName || ''} ${contact.lastName || ''}`;
    } else if (contact.firstName) {
      name = contact.firstName;
    } else if (contact.lastName) {
      name = contact.lastName;
    }
    return name;
  }
}
