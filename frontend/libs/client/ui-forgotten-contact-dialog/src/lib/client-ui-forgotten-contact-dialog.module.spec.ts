import { async, TestBed } from '@angular/core/testing';
import { ClientUiForgottenContactDialogModule } from './client-ui-forgotten-contact-dialog.module';

describe('ClientUiForgottenContactDialogModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientUiForgottenContactDialogModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientUiForgottenContactDialogModule).toBeDefined();
  });
});
