import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';
import {MatDialog} from '@angular/material/dialog';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterComponent ],
      providers: [
        {provide: Router, useValue: jasmine.createSpyObj(['navigate'])},
        {provide: ApiService, useValue: jasmine.createSpyObj(['registerClient', 'checkUsername'])},
        {provide: SnackbarService, useValue: jasmine.createSpyObj(['warning', 'success'])},
        {provide: ActivatedRoute, useValue: {
          snapshot: {
            paramMap: {
              get: (param) => ''
            }
          }
          }},
        {provide: MatDialog, useValue: {}}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
