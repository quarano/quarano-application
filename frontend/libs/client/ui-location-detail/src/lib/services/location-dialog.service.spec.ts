/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { LocationDialogService } from './location-dialog.service';

describe('Service: LocationDialog', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocationDialogService],
    });
  });

  it('should ...', inject([LocationDialogService], (service: LocationDialogService) => {
    expect(service).toBeTruthy();
  }));
});
