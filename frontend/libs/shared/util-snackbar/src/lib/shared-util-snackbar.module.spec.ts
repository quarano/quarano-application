import { async, TestBed } from '@angular/core/testing';
import { SharedUtilSnackbarModule } from './shared-util-snackbar.module';

describe('SharedUtilSnackbarModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilSnackbarModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilSnackbarModule).toBeDefined();
  });
});
