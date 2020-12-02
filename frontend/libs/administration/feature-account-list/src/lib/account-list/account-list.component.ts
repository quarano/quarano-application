import { DeleteButtonComponent } from './delete-button.component';
import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { map, tap } from 'rxjs/operators';
import { AccountDto, AccountEntityService } from '@qro/administration/domain';
import { IRole, roles } from '@qro/auth/api';
import { ApiService } from '@qro/shared/util-data-access';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { ColDef } from 'ag-grid-community';
import { DE_LOCALE, UnorderedListComponent } from '@qro/shared/ui-ag-grid';

interface ViewModel {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  roles: string[];
  _links: any;
  rowHeight: number;
  accountId: string;
}

@Component({
  selector: 'qro-account-list',
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.scss'],
})
export class AccountListComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  accounts$: Observable<ViewModel[]>;
  roles: IRole[] = roles;
  defaultColDef: ColDef = {
    editable: false,
    filter: 'agTextColumnFilter',
    sortable: true,
  };
  columnDefs: ColDef[] = [];
  locale = DE_LOCALE;
  frameworkComponents;

  constructor(
    private router: Router,

    private entityService: AccountEntityService
  ) {
    this.frameworkComponents = { deleteButton: DeleteButtonComponent };
    this.columnDefs = [
      { headerName: 'Nachname', field: 'lastName', flex: 2 },
      { headerName: 'Vorname', field: 'firstName', flex: 2 },
      { headerName: 'Username', field: 'username', flex: 2 },
      { headerName: 'E-Mail', field: 'email', flex: 3 },
      {
        headerName: 'Rollen',
        field: 'roles',
        cellRendererFramework: UnorderedListComponent,
        flex: 3,
      },
      {
        cellRenderer: 'deleteButton',
        field: '_links',
        headerName: 'Löschen',
      },
    ];
  }

  ngOnInit() {
    this.accounts$ = this.entityService.entities$.pipe(
      map((accounts) =>
        accounts.map((a) => ({
          firstName: a.firstName,
          lastName: a.lastName,
          username: a.username,
          email: a.email,
          roles: a.roles.map((r) => this.getRoleDisplayName(r)),
          _links: a._links,
          rowHeight: 50 + a.roles.length * 9,
          accountId: a.accountId,
        }))
      )
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  onSelect(event: any) {
    this.router.navigate(['/administration/accounts/account-detail', event.node.data.accountId]);
  }

  getRoleDisplayName(role: string) {
    return this.roles.find((r) => r.name === role).displayName;
  }

  getRowHeight(params) {
    return params.data.rowHeight;
  }
}
