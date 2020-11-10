import { async, TestBed } from '@angular/core/testing';
import { ClientFeatureContactPersonsModule } from './client-feature-contact-persons.module';

describe('ClientFeatureContactPersonsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientFeatureContactPersonsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientFeatureContactPersonsModule).toBeDefined();
  });
});
