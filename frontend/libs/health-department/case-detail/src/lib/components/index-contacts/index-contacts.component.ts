import { Component, Input, OnInit } from '@angular/core';
import { ContactDto } from '@qro/health-department/domain';

@Component({
  selector: 'qro-client-index-contacts',
  templateUrl: './index-contacts.component.html',
  styleUrls: ['./index-contacts.component.scss'],
})
export class IndexContactsComponent implements OnInit {
  @Input()
  contacts: ContactDto[];

  constructor() {}

  ngOnInit(): void {}
}
