import { ClientDiaryDomainModule } from '@qro/client/diary/domain';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { DiaryTodayListItemComponent } from './components/diary-today-list-item/diary-today-list-item.component';
import { DiaryListItemComponent } from './components/diary-list-item/diary-list-item.component';
import { DiaryEntryWarningComponent } from './components/diary-entry-warning/diary-entry-warning.component';
import { DiaryEntrySuccessComponent } from './components/diary-entry-success/diary-entry-success.component';
import { DiaryResolver } from '@qro/client/diary/domain';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiaryComponent } from './components/diary/diary.component';

@NgModule({
  declarations: [
    DiaryComponent,
    DiaryEntrySuccessComponent,
    DiaryEntryWarningComponent,
    DiaryListItemComponent,
    DiaryTodayListItemComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: DiaryComponent,
        resolve: { diary: DiaryResolver },
      },
    ]),
    SharedUiMaterialModule,
    SharedUiAlertModule,
    ClientDiaryDomainModule,
  ],
})
export class ClientDiaryDiaryListModule {}
