import { Component, OnInit, Input } from '@angular/core';
import { CaseActionDto } from '@models/case-action';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';

@Component({
  selector: 'app-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss']
})
export class ActionComponent implements OnInit {
  @Input() caseAction: CaseActionDto;
  formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      comment: new FormControl()
    });
  }

  submitForm() {

  }

}
