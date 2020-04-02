import { SubSink } from 'subsink';
import { SnackbarService } from '../../services/snackbar.service';
import { ApiService } from '../../services/api.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ContactPersonDto } from 'src/app/models/contact-person';

@Component({
  selector: 'app-contact-person',
  templateUrl: './contact-person.component.html',
  styleUrls: ['./contact-person.component.scss']
})
export class ContactPersonComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  contactPerson: ContactPersonDto;
  private subs = new SubSink();

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get isNew() {
    return (!this.contactPerson.id);
  }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPerson = data.contactPerson;
    }));
    this.buildForm();
  }

  buildForm() {
    this.formGroup = this.formBuilder.group(
      {
        firstname: new FormControl(this.contactPerson.firstname, [Validators.required]),
        surename: new FormControl(this.contactPerson.surename, [Validators.required]),
        email: new FormControl(this.contactPerson.email, [Validators.email]),
        phone: new FormControl(this.contactPerson.phone, [Validators.required]),
      }
    );
  }

  onSubmit() {
    if (this.formGroup.valid) {
      Object.assign(this.contactPerson, this.formGroup.value);

      if (this.isNew) {
        this.createContactPerson();
      } else {
        this.modifyContactPerson();
      }
    }
  }

  createContactPerson() {
    this.subs.add(this.apiService
      .createContactPerson(this.contactPerson)
      .subscribe(_ => {
        this.snackbarService.success('Kontaktperson erfolgreich angelegt');
        this.router.navigate(['/contacts']);
      }));
  }

  modifyContactPerson() {
    this.subs.add(this.apiService
      .modifyContactPerson(this.contactPerson)
      .subscribe(_ => {
        this.snackbarService.success('Kontaktperson erfolgreich aktualisiert');
        this.router.navigate(['/contacts']);
      }));
  }

}
