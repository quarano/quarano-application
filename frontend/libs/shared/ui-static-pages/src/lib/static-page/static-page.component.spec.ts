import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { StaticPageComponent } from './static-page.component';

describe('StaticPageComponent', () => {
  let component: StaticPageComponent;
  let fixture: ComponentFixture<StaticPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StaticPageComponent],
      providers: provideMockStore({}),
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaticPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
