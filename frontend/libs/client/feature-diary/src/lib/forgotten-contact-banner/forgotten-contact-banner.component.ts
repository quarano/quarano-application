import { ContactPersonService } from '@qro/client/domain';
import { SubSink } from 'subsink';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnDestroy } from '@angular/core';
import { ForgottenContactDialogComponent } from '@qro/client/ui-forgotten-contact-dialog';

@Component({
  selector: 'qro-forgotten-contact-banner',
  templateUrl: './forgotten-contact-banner.component.html',
  styleUrls: ['./forgotten-contact-banner.component.scss'],
})
export class ForgottenContactBannerComponent implements OnDestroy {
  private subs = new SubSink();

  constructor(private dialog: MatDialog, private contactPersonService: ContactPersonService) {}

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  openContactDialog() {
    this.subs.add(
      this.contactPersonService.getContactPersons().subscribe((contactPersons) =>
        this.dialog.open(ForgottenContactDialogComponent, {
          disableClose: true,
          data: {
            contactPersons,
          },
        })
      )
    );
  }
}
