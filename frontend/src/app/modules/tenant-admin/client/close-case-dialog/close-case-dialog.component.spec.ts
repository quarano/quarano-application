import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CloseCaseDialogComponent } from './close-case-dialog.component';

describe('CloseCaseDialogComponent', () => {
  let component: CloseCaseDialogComponent;
  let fixture: ComponentFixture<CloseCaseDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloseCaseDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseCaseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
