import { async, TestBed } from '@angular/core/testing';
import { GeneralShellModule } from './general-shell.module';

describe('GeneralShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GeneralShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(GeneralShellModule).toBeDefined();
  });
});
