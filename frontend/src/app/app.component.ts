import { UserService } from '@services/user.service';
import { Component, OnInit } from '@angular/core';
import { ProgressBarService } from '@services/progress-bar.service';
import { delay } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public progressBarActive = false;

  constructor(private progressBarService: ProgressBarService) {
  }

  ngOnInit(): void {
    this.progressBarService.progressBarActive$$
      .pipe(
        delay(0)
      )
      .subscribe(value => {
        this.progressBarActive = value;
      });
  }
}
