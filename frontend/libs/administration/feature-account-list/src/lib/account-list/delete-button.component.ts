import { ApiService } from '@qro/shared/util-data-access';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { AccountDto, AccountEntityService } from '@qro/administration/domain';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';

@Component({
  selector: 'qro-delete-button',
  template: `
    <button
      *ngIf="params.value.delete"
      class="icon-button"
      data-cy="delete-button"
      mat-icon-button
      [matTooltip]="params.data.firstName + ' ' + params.data.lastName + ' löschen'"
      matTooltipClass="qro-tooltip"
      matTooltipPosition="right"
      (click)="deleteUser($event, params.data)"
    >
      <mat-icon>delete_outline</mat-icon>
    </button>
  `,
  styles: [
    `
      .icon-button:hover,
      .icon-button:focus {
        background-color: #c3c3c3;
        color: white;
        cursor: pointer;
      }
    `,
  ],
})
export class DeleteButtonComponent implements ICellRendererAngularComp {
  public params: any;

  agInit(params: any): void {
    this.params = params;
  }

  refresh(): boolean {
    return false;
  }

  constructor(
    private entityService: AccountEntityService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private apiService: ApiService
  ) {}

  deleteUser(event, account: Partial<AccountDto>) {
    event.stopPropagation();
    this.confirmDeletion(account).subscribe((result) => {
      if (result) {
        this.apiService
          .delete(account._links)
          .pipe(tap((_) => this.entityService.removeOneFromCache(account.accountId)))
          .subscribe((_) => {
            this.snackbarService.success(`${account.firstName} ${account.lastName} wurde erfolgreich gelöscht.`);
          });
      }
    });
  }

  confirmDeletion(user: Partial<AccountDto>): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Löschen?',
        text: `Sind Sie sicher, dass Sie ${user.firstName} ${user.lastName} löschen wollen?`,
      },
    });

    return dialogRef.afterClosed().pipe(
      map((result) => {
        return !!result;
      })
    );
  }
}
