/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ContactPersonFormComponent } from './contact-person-form.component';
import {FormBuilder} from '@angular/forms';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';
import {ContactPerson} from '../../../../../../coronareportfrontend/src/app/models/generated/generated-dto';
import {ContactPersonDto} from '@models/contact-person';
import {of} from 'rxjs';

describe('ContactPersonFormComponent', () => {
  let component: ContactPersonFormComponent;
  let fixture: ComponentFixture<ContactPersonFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContactPersonFormComponent ],
      providers: [
        FormBuilder,
        {provide: ApiService, useValue: jasmine.createSpyObj(['modifyContactPerson'])},
        {provide: SnackbarService, useValue: jasmine.createSpyObj(['warning', 'success'])}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactPersonFormComponent);
    component = fixture.componentInstance;
    component.contactPerson = {} as ContactPersonDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
