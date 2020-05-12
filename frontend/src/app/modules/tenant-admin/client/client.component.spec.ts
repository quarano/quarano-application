import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientComponent } from './client.component';
import {RouterTestingModule} from '@angular/router/testing';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';
import {MatDialog} from '@angular/material/dialog';

describe('ClientComponent', () => {
  let component: ClientComponent;
  let fixture: ComponentFixture<ClientComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ClientComponent],
      providers: [
        { provide: MatDialog, useValue: jasmine.createSpyObj([''])},
        {provide: ApiService, useValue: jasmine.createSpyObj(['']) },
        { provide: SnackbarService, useValue: jasmine.createSpyObj(['']) },
        { provide: MatDialog, useValue: {} },
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
