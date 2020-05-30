import { AsideComponent } from './components/aside.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsideHostDirective } from './directives/aside-host.directive';

@NgModule({
  imports: [CommonModule],
  declarations: [AsideComponent, AsideHostDirective],
  exports: [AsideComponent, AsideHostDirective]
})
export class SharedUiAsideModule { }
