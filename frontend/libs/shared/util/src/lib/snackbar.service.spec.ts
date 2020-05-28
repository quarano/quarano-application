/* tslint:disable:no-unused-variable */

import {inject, TestBed} from '@angular/core/testing';
import {SnackbarService} from './snackbar.service';
import {MatSnackBar} from '@angular/material/snack-bar';

describe('Service: Snackbar', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SnackbarService,
        {provide: MatSnackBar, useValue: {}}
      ]
    });
  });

  it('should ...', inject([SnackbarService], (service: SnackbarService) => {
    expect(service).toBeTruthy();
  }));
});
