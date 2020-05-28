import { async, TestBed } from '@angular/core/testing';
import { AdministrationShellModule } from './administration-shell.module';

describe('AdministrationShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationShellModule).toBeDefined();
  });
});
