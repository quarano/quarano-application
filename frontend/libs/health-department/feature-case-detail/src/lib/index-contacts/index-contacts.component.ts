import { ActivatedRoute } from '@angular/router';
import { Component, Input, OnInit } from '@angular/core';
import { ContactDto } from '@qro/health-department/domain';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-client-index-contacts',
  templateUrl: './index-contacts.component.html',
  styleUrls: ['./index-contacts.component.scss'],
})
export class IndexContactsComponent implements OnInit {
  caseIndexContacts$: Observable<ContactDto[]>;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.caseIndexContacts$ = this.route.parent.data.pipe(map((data) => data.case.indexContacts));
  }
}
