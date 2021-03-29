import { SubSink } from 'subsink';
import { TranslateService } from '@ngx-translate/core';
import { ContactPersonDto, LocationDto } from '@qro/client/domain';
import { FormGroup, FormBuilder, FormControl, ValidatorFn } from '@angular/forms';
import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { Observable, of } from 'rxjs';
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';
import { ContactDialogService } from '@qro/client/ui-contact-person-detail';
import { LocationDialogService } from '@qro/client/ui-location-detail';

export interface ISelectItem {
  id: string;
  label$: Observable<string>;
}

@Component({
  selector: 'qro-encounter-form',
  templateUrl: './encounter-form.component.html',
  styleUrls: ['./encounter-form.component.scss'],
})
export class EncounterFormComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  date: Date;
  private subs = new SubSink();
  private _contactPersons: ContactPersonDto[] = [];
  get contactPersons() {
    return this._contactPersons;
  }
  set contactPersons(value: ContactPersonDto[]) {
    this._contactPersons = value;
    this.contactPersonSelectItems = this._contactPersons.map((person) => ({
      id: person.id,
      label$: this.getName(person),
    }));
    this.contactPersonSelectItems.push({
      id: 'new',
      label$: this.translate.stream('ENCOUNTER_FORM.NICHT_IN_LISTE'),
    });
  }
  private _locations: LocationDto[] = [];
  get locations() {
    return this._locations;
  }
  set locations(value: LocationDto[]) {
    this._locations = value;
    this.locationSelectItems = this._locations.map((location) => ({
      id: location.id,
      label$: of(`${location.name} (${location.zipCode} ${location.city})`),
    }));
    this.locationSelectItems.push({
      id: 'new',
      label$: this.translate.stream('ENCOUNTER_FORM.NICHT_IN_LISTE'),
    });
  }
  timepickerTheme: NgxMaterialTimepickerTheme;
  contactPersonSelectItems: ISelectItem[] = [
    { id: 'new', label$: this.translate.stream('ENCOUNTER_FORM.NICHT_IN_LISTE') },
  ];
  locationSelectItems: ISelectItem[] = [];
  contactPersonAdded = new EventEmitter<ContactPersonDto>();
  locationAdded = new EventEmitter<LocationDto>();
  EncounterFormValidator: ValidatorFn = (fg: FormGroup) => {
    const location = fg.get('location')?.value as ISelectItem;
    const contactPersons = fg.get('contactPersons')?.value as ISelectItem[];
    const from = fg.get('from')?.value as string;
    const to = fg.get('to')?.value as string;
    const error: any = {};

    if (!location && contactPersons.length === 0) {
      error.encounterFormError = true;
    }
    if (from && to && to < from) {
      error.timeError = true;
    }

    return error || null;
  };

  constructor(
    private formBuilder: FormBuilder,
    private translate: TranslateService,
    private contactPersonDialogService: ContactDialogService,
    private locationDialogService: LocationDialogService
  ) {
    this.timepickerTheme = {
      container: {
        buttonColor: '#5ce1e6',
      },
      clockFace: {
        clockHandColor: '#5ce1e6',
      },
      dial: {
        dialBackgroundColor: '#5ce1e6',
      },
    };
  }

  ngOnInit() {
    this.createForm();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  private createForm() {
    this.formGroup = this.formBuilder.group(
      {
        contactPersons: new FormControl([]),
        location: new FormControl(null),
        from: new FormControl(null),
        to: new FormControl(null),
      },
      {
        validators: [this.EncounterFormValidator],
      }
    );
  }

  private getName(contact: ContactPersonDto): Observable<string> {
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

  onNewContactPersonClick(event: Event) {
    event.stopPropagation();
    this.subs.add(
      this.contactPersonDialogService
        .openContactPersonDialog({ disableClose: true })
        .afterClosed()
        .subscribe((createdContact: ContactPersonDto | null) => {
          if (createdContact) {
            this.contactPersonAdded.emit(createdContact);
          }
        })
    );
  }

  onNewLocationClick(event: Event) {
    event.stopPropagation();
    this.subs.add(
      this.locationDialogService
        .openLocationDialog({ disableClose: true })
        .afterClosed()
        .subscribe((createdLocation: LocationDto | null) => {
          if (createdLocation) {
            this.locationAdded.emit(createdLocation);
          }
        })
    );
  }

  onFormSubmit() {
    this.formGroup.markAllAsTouched();
    console.log(this.formGroup.hasError('encounterFormError'));
  }
}
