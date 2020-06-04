import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AsideComponent } from './components/aside.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsideHostDirective } from './directives/aside-host.directive';

@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  declarations: [AsideComponent, AsideHostDirective],
  exports: [AsideComponent, AsideHostDirective],
})
export class SharedUiAsideModule {}
