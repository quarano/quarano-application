import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MailComponent } from './mail.component';
import {SnackbarService} from '@services/snackbar.service';
import {DomSanitizer} from '@angular/platform-browser';

describe('MailComponent', () => {
  let component: MailComponent;
  let fixture: ComponentFixture<MailComponent>;

  beforeEach(async(() => {
    const snackbarService = jasmine.createSpyObj(['success', 'warning']);
    TestBed.configureTestingModule({
      declarations: [ MailComponent ],
      providers: [
        {provide: DomSanitizer, useValue: jasmine.createSpyObj(['bypassSecurityTrustHtml'])},
        {provide: SnackbarService, useValue: snackbarService}
      ]
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
