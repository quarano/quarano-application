import { Injectable, Type } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AsideService {
  private asideComponentContent$$: BehaviorSubject<Type<any>> = new BehaviorSubject(null);
  public asideComponentContent$ = this.asideComponentContent$$.asObservable();

  constructor() {}

  public setAsideComponentContent(contentComponent: Type<any>) {
    this.asideComponentContent$$.next(contentComponent);
  }

  public clearAsideComponentContent() {
    this.asideComponentContent$$.next(null);
  }
}
