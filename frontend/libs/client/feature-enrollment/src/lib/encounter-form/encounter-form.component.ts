import { TranslateService } from '@ngx-translate/core';
import { ContactPersonDto, LocationDto } from '@qro/client/domain';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'qro-encounter-form',
  templateUrl: './encounter-form.component.html',
  styleUrls: ['./encounter-form.component.scss'],
})
export class EncounterFormComponent implements OnInit {
  formGroup: FormGroup;
  date: Date;
  contactPersons: ContactPersonDto[] = [];
  locations: LocationDto[] = [];

  constructor(private formBuilder: FormBuilder, private translate: TranslateService) {}

  ngOnInit() {
    this.createForm();
  }

  private createForm() {
    this.formGroup = this.formBuilder.group({
      contactPersons: new FormControl([]),
      locations: new FormControl([]),
    });
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
    return this.translate.stream('CONTACT_PERSONS.ANONYME_KONTAKTPERSON');
  }
}
