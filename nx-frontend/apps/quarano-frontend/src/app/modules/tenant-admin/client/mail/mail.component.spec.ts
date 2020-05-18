import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MailComponent} from './mail.component';
import {DomSanitizer} from '@angular/platform-browser';
import {SnackbarService} from '../../../../services/snackbar.service';
import {NO_ERRORS_SCHEMA} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';

describe('MailComponent', () => {
  let component: MailComponent;
  let fixture: ComponentFixture<MailComponent>;

  beforeEach(async(() => {
    const snackbarService = {
      warning: () => {
      }, success: () => {
      }
    } as any;
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [MailComponent],
      providers: [
        {
          provide: DomSanitizer, useValue: {
            bypassSecurityTrustHtml: () => {
            },
            sanitize: () => {}
          }
        },
        {provide: SnackbarService, useValue: snackbarService}
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
