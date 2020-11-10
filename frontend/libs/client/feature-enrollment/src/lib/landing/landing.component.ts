import { StaticPageKeys } from '@qro/shared/ui-static-pages';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CaseType } from '@qro/auth/api';

@Component({
  selector: 'qro-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  public ClientType = CaseType;
  public userType = CaseType.Index;
  public clientcode: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.userType = (this.route.snapshot.paramMap.get('usertype') as CaseType) || this.userType;
    this.clientcode = this.route.snapshot.paramMap.get('clientcode');
  }

  get pageKey(): StaticPageKeys {
    if (this.userType === CaseType.Contact) {
      return StaticPageKeys.WelcomeContact;
    }
    return StaticPageKeys.WelcomeIndex;
  }

  showIndexNumber() {
    return this.userType === CaseType.Index;
  }
  showContactNumber() {
    return this.userType === CaseType.Contact;
  }
}
