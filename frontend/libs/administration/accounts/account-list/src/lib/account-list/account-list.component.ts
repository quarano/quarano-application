import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { AccountDto } from '@qro/administration/accounts/domain';
import { IRole, roles } from '../../../../../../../apps/quarano-frontend/src/app/models/role';
import { ApiService } from '../../../../../../../apps/quarano-frontend/src/app/services/api.service';
import { ArrayFunctions, SnackbarService } from '@qro/shared/util';
import {
  ConfirmDialogData,
  QroDialogService,
} from '../../../../../../../apps/quarano-frontend/src/app/services/qro-dialog.service';

@Component({
  selector: 'qro-account-list',
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.scss'],
})
export class AccountListComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  accounts: AccountDto[] = [];
  loading = false;
  roles: IRole[] = roles;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dialog: QroDialogService,
    private apiService: ApiService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit() {
    this.loading = true;
    this.subs.add(
      this.route.data.subscribe(
        (data) => {
          this.accounts = data.accounts;
          this.loading = false;
        },
        () => (this.loading = false)
      )
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  onSelect(event) {
    this.router.navigate(['/administration/accounts/account-detail/edit', event?.selected[0]?.accountId]);
  }

  getRoleDisplayName(role: string) {
    return this.roles.find((r) => r.name === role).displayName;
  }

  deleteUser(event, user: AccountDto) {
    event.stopPropagation();
    this.confirmDeletion(user).subscribe((result) => {
      if (result) {
        this.apiService.delete(user._links).subscribe((_) => {
          this.snackbarService.success(`${user.firstName} ${user.lastName} wurde erfolgreich gelöscht.`);
          this.accounts = ArrayFunctions.remove(this.accounts, user);
        });
      }
    });
  }

  confirmDeletion(user: AccountDto): Observable<boolean> {
    const data: ConfirmDialogData = {
      title: 'Löschen?',
      text: `Sind Sie sicher, dass Sie ${user.firstName} ${user.lastName} löschen wollen?`,
    };
    return this.dialog
      .openConfirmDialog({ data: data })
      .afterClosed()
      .pipe(
        map((result) => {
          return !!result;
        })
      );
  }
}
