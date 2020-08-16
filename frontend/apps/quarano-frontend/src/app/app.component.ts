import { SubSink } from 'subsink';
import { LanguageService } from '@qro/shared/util-translation';
import { TranslateService } from '@ngx-translate/core';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { delay } from 'rxjs/operators';
import { ProgressBarService } from '@qro/shared/util-progress-bar';
import { noop } from 'rxjs';

@Component({
  selector: 'qro-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  public progressBarActive = false;
  private subs = new SubSink();

  constructor(
    private progressBarService: ProgressBarService,
    private translate: TranslateService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    this.subs.add(
      this.progressBarService.progressBarActive$$.pipe(delay(0)).subscribe((value) => {
        this.progressBarActive = value;
      })
    );
    this.initializeTranslation();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  private initializeTranslation() {
    let lang = localStorage.getItem('selectedLanguage');
    if (lang) {
      lang = JSON.parse(lang)?.key;
    }
    if (!lang) {
      lang = this.translate.getBrowserLang();
    }

    this.subs.add(this.languageService.init(lang).subscribe(noop));
  }
}
