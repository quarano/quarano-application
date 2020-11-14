import { TestBed, inject } from '@angular/core/testing';
import { EntityCollectionServiceElementsFactory, HttpUrlGenerator } from '@ngrx/data';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AccountEntityService } from './account-entity.service';

describe('Service: AccountEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AccountEntityService,
        { provide: EntityCollectionServiceElementsFactory, useValue: {} },
        { provide: HttpUrlGenerator, useValue: {} },
      ],
      imports: [HttpClientTestingModule],
    });
  });

  xit('should ...', inject([AccountEntityService], (service: AccountEntityService) => {
    expect(service).toBeTruthy();
  }));
});
