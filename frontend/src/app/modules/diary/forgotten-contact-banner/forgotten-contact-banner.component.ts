import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';
import { ForgottenContactDialogComponent } from '../forgotten-contact-dialog/forgotten-contact-dialog.component';

@Component({
  selector: 'app-forgotten-contact-banner',
  templateUrl: './forgotten-contact-banner.component.html',
  styleUrls: ['./forgotten-contact-banner.component.scss']
})
export class ForgottenContactBannerComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }

  openContactDialog() {
    this.dialog.open(ForgottenContactDialogComponent, {
      height: '90vh',
      maxWidth: '100vw',
      data: {
      }
    });
  }

}
