import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ContactDto, CaseEntityService } from '@qro/health-department/domain';
import { Observable, combineLatest } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'qro-client-index-contacts',
  templateUrl: './index-contacts.component.html',
  styleUrls: ['./index-contacts.component.scss'],
})
export class IndexContactsComponent implements OnInit {
  caseIndexContacts$: Observable<ContactDto[]>;

  constructor(private route: ActivatedRoute, private entityService: CaseEntityService) {}

  ngOnInit(): void {
    this.caseIndexContacts$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      map(([id, entityMap]) => {
        return entityMap[id].indexContacts;
      }),
      shareReplay(1)
    );
  }
}
