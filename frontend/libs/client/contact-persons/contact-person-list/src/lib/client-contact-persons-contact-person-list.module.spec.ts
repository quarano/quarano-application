import { async, TestBed } from '@angular/core/testing';
import { ClientContactPersonsContactPersonListModule } from './client-contact-persons-contact-person-list.module';

describe('ClientContactPersonsContactPersonListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientContactPersonsContactPersonListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientContactPersonsContactPersonListModule).toBeDefined();
  });
});
