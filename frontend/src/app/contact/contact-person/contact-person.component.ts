import { SubSink } from 'subsink';
import {
  Component, OnInit, OnDestroy
} from '@angular/core';

import {
  ActivatedRoute
} from '@angular/router';
import { ContactPersonDto } from 'src/app/models/contact-person';
import { Location } from '@angular/common';


@Component({
  selector: 'app-contact-person',
  templateUrl: './contact-person.component.html',
  styleUrls: ['./contact-person.component.scss']
})
export class ContactPersonComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  contactPerson: ContactPersonDto;

  constructor(
    private route: ActivatedRoute,
    private location: Location) { }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPerson = data.contactPerson;
    }));
  }

  navigateBack() {
    this.location.back();
  }
}
