import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingComponent } from './components/landing/landing.component';

const routes: Routes = [
  { path: ':usertype/:clientcode', component: LandingComponent },
  { path: '', component: LandingComponent, pathMatch: 'full' },
];

@NgModule({
  declarations: [LandingComponent],
  imports: [CommonModule, RouterModule.forChild(routes), SharedUiMaterialModule],
})
export class ClientEnrollmentLandingModule {}
