import {Component, Inject, OnInit} from '@angular/core';
import {HealthDepartmentDto} from '@models/healthDepartment';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-hd-contact',
  templateUrl: './hd-contact.component.html',
  styleUrls: ['./hd-contact.component.scss']
})
export class HdContactComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: HealthDepartmentDto) {
  }

  ngOnInit(): void {
  }

}
