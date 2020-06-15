import { async, TestBed } from '@angular/core/testing';
import { ClientProfileModule } from './client-profile.module';

describe('ClientProfileModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientProfileModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientProfileModule).toBeDefined();
  });
});
