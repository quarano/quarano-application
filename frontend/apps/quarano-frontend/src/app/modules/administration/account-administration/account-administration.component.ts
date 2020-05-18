import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { map } from 'rxjs/operators';
import {AccountDto} from '../../../models/account';
import {IRole, roles} from '../../../models/role';
import {ApiService} from '../../../services/api.service';
import {SnackbarService} from '../../../services/snackbar.service';
import {ConfirmationDialogComponent} from '../../../ui/confirmation-dialog/confirmation-dialog.component';
import '../../../utils/array-extensions';

@Component({
  selector: 'qro-account-administration',
  templateUrl: './account-administration.component.html',
  styleUrls: ['./account-administration.component.scss']
})
export class AccountAdministrationComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  accounts: AccountDto[] = [];
  loading = false;
  roles: IRole[] = roles;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private apiService: ApiService,
    private snackbarService: SnackbarService) { }

  ngOnInit() {
    this.loading = true;
    this.subs.add(this.route.data.subscribe(
      data => {
        this.accounts = data.accounts;
        this.loading = false;
      },
      () => this.loading = false));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  onSelect(event) {
    this.router.navigate(
      ['/administration/accounts/edit', event?.selected[0]?.accountId]);
  }

  getRoleDisplayName(role: string) {
    return this.roles.find(r => r.name === role).displayName;
  }

  deleteUser(event, user: AccountDto) {
    event.stopPropagation();
    this.confirmDeletion(user)
      .subscribe(result => {
        if (result) {
          this.apiService.delete(user._links)
            .subscribe(_ => {
              this.snackbarService.success(`${user.firstName} ${user.lastName} wurde erfolgreich gelöscht.`);
              this.accounts = this.accounts.remove(user);
            });
        }
      });
  }

  confirmDeletion(user: AccountDto): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Löschen?',
        text:
          `Sind Sie sicher, dass Sie ${user.firstName} ${user.lastName} löschen wollen?`
      }
    });

    return dialogRef.afterClosed().pipe(
      map(result => {
        return !!result;
      })
    );
  }
}
