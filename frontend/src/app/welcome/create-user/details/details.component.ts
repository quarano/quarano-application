import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Client} from '../../../models/client';

@Component({
  selector: 'app-create-user-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  @Output() client = new EventEmitter<Client>();

  // ToDo Error Messages

  public nameFormGroup = new FormGroup({
    surname: new FormControl(null, [Validators.minLength(2), Validators.maxLength(100)]),
    firstname: new FormControl(null, [Validators.minLength(2), Validators.maxLength(100)])
  });

  public phoneZipFormGroup = new FormGroup({
    phone: new FormControl(null, [Validators.minLength(3), Validators.maxLength(25)]),
    zipCode: new FormControl(null, [Validators.minLength(5), Validators.maxLength(5)])
  });

  constructor() { }

  ngOnInit(): void {
  }

  public saveUser() {
    const client: Client = {
      firstname: this.nameFormGroup.controls['firstname'].value,
      surename: this.nameFormGroup.controls['surname'].value,
      phone: this.phoneZipFormGroup.controls['phone'].value,
      zipCode: this.phoneZipFormGroup.controls['zipCode'].value,
      clientId: null,
      infected: false,
      healthDepartmentId: null
    };

    this.client.emit(client);
  }

}
