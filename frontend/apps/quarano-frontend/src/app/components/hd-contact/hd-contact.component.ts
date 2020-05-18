import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {HealthDepartmentDto} from '../../models/healthDepartment';

@Component({
  selector: 'qro-hd-contact',
  templateUrl: './hd-contact.component.html',
  styleUrls: ['./hd-contact.component.scss']
})
export class HdContactComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: HealthDepartmentDto) {
  }

  ngOnInit(): void {
  }

}
