/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AsideHostDirective } from '@directives/aside-host.directive';
import { AsideService } from '@services/aside.service';
import { AsideComponent } from './aside.component';


describe('AsideComponent', () => {
  let component: AsideComponent;
  let fixture: ComponentFixture<AsideComponent>;

  beforeEach(async(() => {

    TestBed.configureTestingModule({
      declarations: [ AsideHostDirective, AsideComponent ],
      providers: [
        { provide: AsideService, useValue: new AsideService() }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AsideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
