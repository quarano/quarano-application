import { async, TestBed } from '@angular/core/testing';
import { ClientDiaryForgottenContactModule } from './client-diary-forgotten-contact.module';

describe('ClientDiaryForgottenContactModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDiaryForgottenContactModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDiaryForgottenContactModule).toBeDefined();
  });
});
