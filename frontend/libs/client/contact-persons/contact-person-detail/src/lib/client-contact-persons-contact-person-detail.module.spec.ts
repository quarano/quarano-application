import { async, TestBed } from '@angular/core/testing';
import { ClientContactPersonsContactPersonDetailModule } from './client-contact-persons-contact-person-detail.module';

describe('ClientContactPersonsContactPersonDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientContactPersonsContactPersonDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientContactPersonsContactPersonDetailModule).toBeDefined();
  });
});
