import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'diary-list',
    pathMatch: 'full',
  },
  {
    path: 'diary-list',
    loadChildren: () => import('@qro/client/diary/diary-list').then((m) => m.ClientDiaryDiaryListModule),
  },
  {
    path: 'diary-detail',
    loadChildren: () => import('@qro/client/diary/diary-detail').then((m) => m.ClientDiaryDiaryDetailModule),
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class ClientDiaryShellModule {}
