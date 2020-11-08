import { ClientStore } from '../store/client-store.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';
import { ProfileService } from './profile.service';
import { API_URL } from '@qro/shared/util-data-access';

describe('Service: Client', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProfileService, { provide: API_URL, useValue: '' }, { provide: ClientStore, useValue: {} }],
    });
  });

  it('should ...', inject([ProfileService], (service: ProfileService) => {
    expect(service).toBeTruthy();
  }));
});
