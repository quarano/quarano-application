import { async, TestBed } from '@angular/core/testing';
import { SharedUiConfirmationDialogModule } from './shared-ui-confirmation-dialog.module';

describe('SharedUiConfirmationDialogModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiConfirmationDialogModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiConfirmationDialogModule).toBeDefined();
  });
});
