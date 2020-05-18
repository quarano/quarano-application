import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ClientsComponent} from './clients.component';
import {RouterTestingModule} from '@angular/router/testing';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('ClientsComponent', () => {
  let component: ClientsComponent;
  let fixture: ComponentFixture<ClientsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ClientsComponent],
      providers: [
        {provide: ActivatedRoute, useValue: { data: of({ cases: [] })}}
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
