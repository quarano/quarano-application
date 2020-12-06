import { map } from 'rxjs/operators';
import { Store, select } from '@ngrx/store';
import { HdContactComponent } from '@qro/client/api';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable, combineLatest } from 'rxjs';
import { HealthDepartmentService } from '@qro/health-department/api';
import { HealthDepartmentDto, UserService } from '@qro/auth/api';
import { ClientStore } from '@qro/client/api';
import { ILanguageConfig, LanguageSelectors, LanguageActions } from '@qro/shared/util-translation';
import { VersionDto, VersionService } from '@qro/general/api';

@Component({
  selector: 'qro-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss'],
})
export class HeaderRightComponent implements OnInit {
  public healthDepartment$: Observable<HealthDepartmentDto> = this.healthDepartmentService.healthDepartment$;
  public currentUserName$ = this.userService.nameOfCurrentUser$;
  selectedLanguage$: Observable<ILanguageConfig>;
  languages$: Observable<ILanguageConfig[]>;
  version$: Observable<VersionDto>;

  constructor(
    public userService: UserService,
    private healthDepartmentService: HealthDepartmentService,
    public clientStore: ClientStore,
    private matDialog: MatDialog,
    private store: Store,
    private versionService: VersionService
  ) {}

  ngOnInit(): void {
    this.selectedLanguage$ = this.store.pipe(select(LanguageSelectors.selectedLanguage));

    this.languages$ = combineLatest([
      this.selectedLanguage$,
      this.store.pipe(select(LanguageSelectors.supportedLanguages)),
    ]).pipe(
      map(([selectedLang, langs]) => {
        if (selectedLang) {
          return langs.filter((l) => l.key !== selectedLang.key);
        }
        return null;
      })
    );
  }

  loadVersion() {
    this.version$ = this.versionService.getVersion();
  }

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, { data: department, maxWidth: 600 });
  }

  changeLanguageAuthenticated(language: ILanguageConfig) {
    this.store.dispatch(LanguageActions.languageSelectedAuthenticatedUser({ selectedLanguage: language }));
  }

  changeLanguageAnonymous(language: ILanguageConfig) {
    this.store.dispatch(LanguageActions.languageSelectedAnonymousUser({ selectedLanguage: language }));
  }

  isMobile() {
    return window.innerWidth <= 993;
  }
}
