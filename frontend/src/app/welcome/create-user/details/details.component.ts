import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ClientDto } from '../../../models/client';

@Component({
  selector: 'app-create-user-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  @Output() client = new EventEmitter<ClientDto>();

  // ToDo Error Messages

  public nameFormGroup = new FormGroup({
    lastName: new FormControl(null, [Validators.minLength(2), Validators.maxLength(100)]),
    firstName: new FormControl(null, [Validators.minLength(2), Validators.maxLength(100)])
  });

  public phoneZipFormGroup = new FormGroup({
    phone: new FormControl(null, [Validators.minLength(3), Validators.maxLength(25)]),
    zipCode: new FormControl(null, [Validators.minLength(5), Validators.maxLength(5)])
  });

  constructor() { }

  ngOnInit(): void {
  }

  public saveUser() {
    /*const client: Client = {
      firstName: this.nameFormGroup.controls.firstName.value,
      lastName: this.nameFormGroup.controls.lastName.value,
      phone: this.phoneZipFormGroup.controls.phone.value,
      zipCode: this.phoneZipFormGroup.controls.zipCode.value,
      clientId: null,
      infected: false,
      healthDepartmentId: null,
      dateOfBirth: null,
      street: null,
      city: null,
      houseNumber: null,
      email: null,
      mobilePhone: null
    };

    this.client.emit(client);*/
  }

}
