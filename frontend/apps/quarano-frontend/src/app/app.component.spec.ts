import { LanguageService } from '@qro/shared/util-translation';
import { async, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ProgressBarService } from '@qro/shared/util-progress-bar';

describe('AppComponent', () => {
  let progressBarService: ProgressBarService;

  beforeEach(async(() => {
    progressBarService = new ProgressBarService();
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [
        { provide: ProgressBarService, useValue: progressBarService },
        { provide: LanguageService, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
