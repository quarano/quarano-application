import { ContactPersonService } from '@qro/client/contact-persons/api';
import { AsideService } from '@qro/shared/ui-aside';
import { ForgottenContactDialogComponent } from '@qro/client/diary/forgotten-contact';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { ForgottenContactBannerComponent } from '@qro/client/diary/forgotten-contact';
import { MatDialog } from '@angular/material/dialog';
import { DiaryDto } from '@qro/client/diary/domain';

@Component({
  selector: 'qro-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.scss'],
})
export class DiaryComponent implements OnInit, OnDestroy {
  diary: DiaryDto;
  private subs = new SubSink();

  constructor(
    private route: ActivatedRoute,
    private asideService: AsideService,
    private dialog: MatDialog,
    private contactPersonService: ContactPersonService
  ) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.diary = data.diary;
      })
    );
    this.asideService.setAsideComponentContent(ForgottenContactBannerComponent);
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
    this.asideService.clearAsideComponentContent();
  }

  openContactDialog() {
    this.subs.add(
      this.contactPersonService.getContactPersons().subscribe((contactPersons) =>
        this.dialog.open(ForgottenContactDialogComponent, {
          data: {
            contactPersons,
          },
        })
      )
    );
  }
}
