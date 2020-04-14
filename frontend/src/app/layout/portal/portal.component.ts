import { Component, OnInit } from '@angular/core';
import {ProgressBarService} from '../../services/progress-bar.service';
import {delay} from 'rxjs/operators';

@Component({
  selector: 'app-portal',
  templateUrl: './portal.component.html',
  styleUrls: ['./portal.component.scss']
})
export class PortalComponent implements OnInit {
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
