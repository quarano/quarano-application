import { async, TestBed } from '@angular/core/testing';
import { ClientShellModule } from './client-shell.module';

describe('ClientShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientShellModule).toBeDefined();
  });
});
