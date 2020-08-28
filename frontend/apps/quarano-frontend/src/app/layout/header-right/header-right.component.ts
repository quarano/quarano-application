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

  constructor(
    public userService: UserService,
    private healthDepartmentService: HealthDepartmentService,
    public clientStore: ClientStore,
    private matDialog: MatDialog,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.selectedLanguage$ = this.store.pipe(select(LanguageSelectors.selectedLanguage));

    this.languages$ = combineLatest([
      this.selectedLanguage$,
      this.store.pipe(select(LanguageSelectors.supportedLanguages)),
    ]).pipe(
      map(([selectedLang, langs]) => {
        console.log(langs, selectedLang);
        return langs.filter((l) => l.key !== selectedLang.key);
      })
    );
  }

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, { data: department, maxWidth: 600 });
  }

  changeLanguage(language: ILanguageConfig) {
    this.store.dispatch(LanguageActions.languageSelected({ selectedLanguage: language }));
  }
}
