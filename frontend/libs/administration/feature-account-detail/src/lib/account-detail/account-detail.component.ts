import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountDto, AccountEntityService } from '@qro/administration/domain';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'qro-account-detail',
  templateUrl: './account-detail.component.html',
  styleUrls: ['./account-detail.component.scss'],
})
export class AccountDetailComponent implements OnInit {
  account$: Observable<AccountDto>;

  constructor(private route: ActivatedRoute, private entityService: AccountEntityService) {}

  ngOnInit() {
    this.account$ = this.route.paramMap.pipe(
      switchMap((params) => this.entityService.loadOneFromStore(params.get('id')))
    );
  }
}
