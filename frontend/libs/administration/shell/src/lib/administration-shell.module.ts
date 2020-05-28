import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [CommonModule, RouterModule.forChild([
    {
      path: '',
      redirectTo: 'accounts',
      pathMatch: 'full'
    },
    {
      path: 'accounts', loadChildren: () =>
        import('@qro/administration/accounts/shell').then(m => m.AdministrationAccountsShellModule)
    }
  ])],
})
export class AdministrationShellModule { }
