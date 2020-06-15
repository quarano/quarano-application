import { Component, OnInit } from '@angular/core';
import { delay } from 'rxjs/operators';
import { ProgressBarService } from '@qro/shared/util-progress-bar';

@Component({
  selector: 'qro-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  public progressBarActive = false;

  constructor(private progressBarService: ProgressBarService) {}

  ngOnInit(): void {
    this.progressBarService.progressBarActive$$.pipe(delay(0)).subscribe((value) => {
      this.progressBarActive = value;
    });
  }
}
