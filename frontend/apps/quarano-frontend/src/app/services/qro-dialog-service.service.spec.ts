import { TestBed } from '@angular/core/testing';

import { QroDialogServiceService } from './qro-dialog-service.service';

describe('QroDialogServiceService', () => {
  let service: QroDialogServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QroDialogServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
