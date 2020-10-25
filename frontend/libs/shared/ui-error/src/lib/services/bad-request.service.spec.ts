import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { TestBed, inject } from '@angular/core/testing';
import { BadRequestService } from './bad-request.service';

describe('Service: BadRequest', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BadRequestService,
        { provide: TranslatedSnackbarService, useValue: {} },
        { provide: SnackbarService, useValue: {} },
      ],
    });
  });

  it('should ...', inject([BadRequestService], (service: BadRequestService) => {
    expect(service).toBeTruthy();
  }));
});
