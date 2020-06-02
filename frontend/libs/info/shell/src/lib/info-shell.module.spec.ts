import { async, TestBed } from '@angular/core/testing';
import { InfoShellModule } from './info-shell.module';

describe('InfoShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [InfoShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(InfoShellModule).toBeDefined();
  });
});
