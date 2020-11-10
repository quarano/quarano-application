import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProgressBarService {
  public progressBarActive$$ = new BehaviorSubject<boolean>(false);

  set progressBarState(state: boolean) {
    this.progressBarActive$$.next(state);
  }

  constructor() {}
}
