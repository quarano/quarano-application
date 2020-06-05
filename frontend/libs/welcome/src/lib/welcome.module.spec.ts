import { async, TestBed } from '@angular/core/testing';
import { WelcomeModule } from './welcome.module';

describe('WelcomeModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [WelcomeModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(WelcomeModule).toBeDefined();
  });
});
