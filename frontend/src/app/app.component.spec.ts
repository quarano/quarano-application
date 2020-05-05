import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {ProgressBarService} from '@services/progress-bar.service';
import {UserService} from '@services/user.service';
import {BehaviorSubject, of} from 'rxjs';

describe('AppComponent', () => {
  let progressBarService: ProgressBarService;

  beforeEach(async(() => {
    progressBarService = new ProgressBarService();
    TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: ProgressBarService, useValue: progressBarService },
      ]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

});
