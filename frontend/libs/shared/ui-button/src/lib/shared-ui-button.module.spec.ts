import { async, TestBed } from '@angular/core/testing';
import { SharedUiButtonModule } from './shared-ui-button.module';

describe('SharedUiButtonModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiButtonModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiButtonModule).toBeDefined();
  });
});
