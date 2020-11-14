import { DeactivatableComponent } from '@qro/shared/util-forms';
import { Component, HostListener, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'qro-account-reset-password',
  templateUrl: './account-reset-password.component.html',
  styleUrls: ['./account-reset-password.component.scss'],
})
export class AccountResetPasswordComponent implements OnInit, DeactivatableComponent {
  constructor() {}

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return true;
  }

  ngOnInit() {}
}
