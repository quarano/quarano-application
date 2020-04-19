import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appAsideHost]'
})
export class AsideHostDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
