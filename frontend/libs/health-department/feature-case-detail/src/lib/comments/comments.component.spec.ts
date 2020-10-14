import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentsComponent } from './comments.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

describe('CommentsComponent', () => {
  let component: CommentsComponent;
  let fixture: ComponentFixture<CommentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CommentsComponent],
      providers: [
        { provide: ActivatedRoute, useValue: {} },
        { provide: HttpClient, useValue: {} },
        { provide: ActivatedRoute, useValue: {} },
        { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
