import {async, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {ProgressBarService} from './services/progress-bar.service';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('AppComponent', () => {
  let progressBarService: ProgressBarService;

  beforeEach(async(() => {
    progressBarService = new ProgressBarService();
    TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      providers: [
        {provide: ProgressBarService, useValue: progressBarService},
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

});
