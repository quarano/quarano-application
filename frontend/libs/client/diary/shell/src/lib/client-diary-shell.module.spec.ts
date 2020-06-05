import { async, TestBed } from '@angular/core/testing';
import { ClientDiaryShellModule } from './client-diary-shell.module';

describe('ClientDiaryShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDiaryShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDiaryShellModule).toBeDefined();
  });
});
