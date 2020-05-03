import { SnackbarService } from '@services/snackbar.service';
import { ApiService } from '@services/api.service';
import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { UserListItemDto } from '@models/user';
import { SubSink } from 'subsink';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ConfirmationDialogComponent } from '@ui/confirmation-dialog/confirmation-dialog.component';
import { map } from 'rxjs/operators';
import '@utils/array-extensions';

@Component({
  selector: 'app-user-administration',
  templateUrl: './user-administration.component.html',
  styleUrls: ['./user-administration.component.scss']
})
export class UserAdministrationComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  users: UserListItemDto[] = [];
  loading = false;

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
        this.users = data.users;
        this.loading = false;
      },
      () => this.loading = false));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  onSelect(event) {
    this.router.navigate(
      ['edit', event?.selected[0]?.id]);
  }

  deleteUser(event, user: UserListItemDto) {
    event.stopPropagation();
    this.confirmDeletion(user)
      .subscribe(result => {
        if (result) {
          this.apiService.delete(user._links)
            .subscribe(_ => {
              this.snackbarService.success(`${user.firstName} ${user.lastName} wurde erfolgreich gelöscht.`);
              this.users.remove(user);
            });
        }
      });
  }

  confirmDeletion(user: UserListItemDto): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Löschen?',
        text:
          `Sind Sie sicher, dass Sie ${user.firstName} ${user.lastName} löschen wollen?`
      }
    });

    return dialogRef.afterClosed().pipe(
      map(result => {
        return result ? true : false;
      })
    );
  }
}
