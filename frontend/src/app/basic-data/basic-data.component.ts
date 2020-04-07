import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { ContactPersonDto } from 'src/app/models/contact-person';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import '../utils/date-extensions';
import { KeyValue } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ContactPersonDialogComponent } from '../contact/contact-person-dialog/contact-person-dialog.component';

@Component({
  selector: 'app-basic-data',
  templateUrl: './basic-data.component.html',
  styleUrls: ['./basic-data.component.scss']
})
export class BasicDataComponent implements OnInit, OnDestroy {
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup: FormGroup;
  retrospectiveContacts = new Map<Date, number[]>();
  contactPersons: ContactPersonDto[] = [];
  subs = new SubSink();
  today = new Date();
  dayOfFirstSymptoms = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate()); // ToDo: take from step 1

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPersons = data.contactPersons;
    }));
    this.buildForms();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForms() {
    this.firstFormGroup = this.formBuilder.group({
      // ToDo: Basic Data Step 1
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this.formBuilder.group({
      // ToDo: Basic Data Step 2
      secondCtrl: ['', Validators.required]
    });
    this.thirdFormGroup = this.formBuilder.group({
    });
    let day = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate());
    const firstDay = this.dayOfFirstSymptoms.addDays(-2);
    while (day >= firstDay) {
      this.retrospectiveContacts.set(day, []);
      this.thirdFormGroup.addControl(day.toLocaleDateString(), new FormControl([]));
      day = day.addDays(-1);
    }
  }

  descendingOrder = (a: KeyValue<Date, number[]>, b: KeyValue<Date, number[]>): number => {
    return a.key > b.key ? -1 : (b.key > a.key ? 1 : 0);
  }

  openContactDialog() {
    const dialogRef = this.dialog.open(ContactPersonDialogComponent, {
      height: '90vh',
      data: {
        contactPerson: { id: null, surename: null, firstname: null, phone: null, email: null },
      }
    });

    this.subs.add(dialogRef.afterClosed().subscribe((createdContact: ContactPersonDto | null) => {
      if (createdContact) {
        this.contactPersons.push(createdContact);
      }
    }));
  }

  onContactAdded(date: Date, id: number) {
    alert(`Added contact ${id} for ${date}`);
    // ToDo: call api post
  }

  onContactRemoved(date: Date, id: number) {
    alert(`Removed contact ${id} for ${date}`);
    // ToDo: call api delete
  }
}
