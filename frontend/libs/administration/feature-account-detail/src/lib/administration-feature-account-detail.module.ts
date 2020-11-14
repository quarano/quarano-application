import { AccountDetailComponent } from './account-detail/account-detail.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { AccountDetailResolver, AdministrationDomainModule } from '@qro/administration/domain';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { PreventUnsavedChangesGuard } from '@qro/shared/util-forms';
import { AccountResetPasswordComponent } from './account-reset-password/account-reset-password.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'new',
  },
  {
    path: 'new',
    component: AccountDetailComponent,
    children: [
      {
        path: '',
        redirectTo: 'edit',
        pathMatch: 'full',
      },
      {
        path: 'edit',
        component: AccountEditComponent,
        canDeactivate: [PreventUnsavedChangesGuard],
      },
    ],
  },
  {
    path: ':id',
    component: AccountDetailComponent,
    resolve: { account: AccountDetailResolver },
    runGuardsAndResolvers: 'always',
    children: [
      {
        path: '',
        redirectTo: 'edit',
        pathMatch: 'full',
      },
      {
        path: 'edit',
        component: AccountEditComponent,
        canDeactivate: [PreventUnsavedChangesGuard],
      },
      {
        path: 'reset-password',
        component: AccountResetPasswordComponent,
        canDeactivate: [PreventUnsavedChangesGuard],
      },
    ],
  },
];

@NgModule({
  declarations: [AccountEditComponent, AccountDetailComponent, AccountResetPasswordComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    AdministrationDomainModule,
    RouterModule.forChild(routes),
  ],
})
export class AdministrationFeatureAccountDetailModule {}
