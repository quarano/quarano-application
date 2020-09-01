import { TranslateTestingModule } from '@qro/shared/util-translation';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ForgottenContactBannerComponent } from './forgotten-contact-banner.component';
import { MatDialog } from '@angular/material/dialog';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ContactPersonService } from '@qro/client/domain';

describe('ForgottenContactBannerComponent', () => {
  let component: ForgottenContactBannerComponent;
  let fixture: ComponentFixture<ForgottenContactBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      declarations: [ForgottenContactBannerComponent],
      providers: [
        { provide: MatDialog, useValue: {} },
        { provide: ContactPersonService, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForgottenContactBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
