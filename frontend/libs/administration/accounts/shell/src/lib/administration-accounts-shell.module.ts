import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, RouterModule.forChild([
    {
      path: '',
      redirectTo: 'account-list',
      pathMatch: 'full'
    },
    {
      path: 'account-list', loadChildren: () =>
        import('@qro/administration/accounts/account-list')
          .then(m => m.AdministrationAccountsAccountListModule)
    },
    {
      path: 'account-detail', loadChildren: () =>
        import('@qro/administration/accounts/account-detail')
          .then(m => m.AdministrationAccountsAccountDetailModule)
    }
  ])],
})
export class AdministrationAccountsShellModule { }
