import {SubSink} from 'subsink';
import {MatDialog} from '@angular/material/dialog';
import {Component, OnDestroy} from '@angular/core';
import {ForgottenContactDialogComponent} from '../forgotten-contact-dialog/forgotten-contact-dialog.component';
import {ApiService} from '../../../services/api.service';

@Component({
  selector: 'qro-forgotten-contact-banner',
  templateUrl: './forgotten-contact-banner.component.html',
  styleUrls: ['./forgotten-contact-banner.component.scss']
})
export class ForgottenContactBannerComponent implements OnDestroy {
  private subs = new SubSink();

  constructor(
    private dialog: MatDialog,
    private apiService: ApiService) {
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  openContactDialog() {
    this.subs.add(this.apiService.getContactPersons()
      .subscribe(contactPersons => this.dialog.open(ForgottenContactDialogComponent, {
        data: {
          contactPersons
        }
      })));
  }
}
