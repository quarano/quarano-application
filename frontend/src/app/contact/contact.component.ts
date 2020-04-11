import { SubSink } from 'subsink';
import { Component, OnInit } from '@angular/core';
import { ContactPersonDto } from '../models/contact-person';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contacts: ContactPersonDto[] = [];
  private subs = new SubSink();

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contacts = data.contacts;
      this.contacts.forEach(c => {
        c.city = 'Buxdehude';
        c.zipCode = '12345';
        c.mobilePhone = '0163-586624322';
        c.identificationHint = 'Die Person ist froh wie der Mops im Haferstroh';
        c.street = 'Besserwisserstraße';
        c.houseNumber = '1232a';
      });
    }));
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
