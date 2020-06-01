import { async, TestBed } from '@angular/core/testing';
import { ClientContactPersonsDomainModule } from './client-contact-persons-domain.module';

describe('ClientContactPersonsDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientContactPersonsDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientContactPersonsDomainModule).toBeDefined();
  });
});
