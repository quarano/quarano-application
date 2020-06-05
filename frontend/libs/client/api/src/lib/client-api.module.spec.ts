import { async, TestBed } from '@angular/core/testing';
import { ClientApiModule } from './client-api.module';

describe('ClientApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientApiModule).toBeDefined();
  });
});
