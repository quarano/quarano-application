import {Component} from '@angular/core';
import {ProgressBarService} from './services/progress-bar.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  public progressBarActive$$ = this.progressBarService.progressBarActive$$;

  constructor(private progressBarService: ProgressBarService) {
  }
}
