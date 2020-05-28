import { async, TestBed } from '@angular/core/testing';
import { SharedUiAlertModule } from './shared-ui-alert.module';

describe('SharedUiAlertModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiAlertModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiAlertModule).toBeDefined();
  });
});
