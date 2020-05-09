import {Component, Input, OnInit} from '@angular/core';
import {ContactDto} from '@models/contact';

@Component({
  selector: 'app-client-index-contacts',
  templateUrl: './index-contacts.component.html',
  styleUrls: ['./index-contacts.component.scss']
})
export class IndexContactsComponent implements OnInit {
  @Input()
  contacts: ContactDto[];

  constructor() { }

  ngOnInit(): void {
  }

}
