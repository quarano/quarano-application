import { async, TestBed } from '@angular/core/testing';
import { ClientContactPersonsShellModule } from './client-contact-persons-shell.module';

describe('ClientContactPersonsShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientContactPersonsShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientContactPersonsShellModule).toBeDefined();
  });
});
