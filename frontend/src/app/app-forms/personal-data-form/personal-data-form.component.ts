import { FormGroup } from '@angular/forms';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-personal-data-form',
  templateUrl: './personal-data-form.component.html',
  styleUrls: ['./personal-data-form.component.css']
})
export class PersonalDataFormComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  @Input() formGroup: FormGroup;

  constructor() { }

  ngOnInit() {
  }

}
