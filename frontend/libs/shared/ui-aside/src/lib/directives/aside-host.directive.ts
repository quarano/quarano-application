import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[qroAsideHost]',
})
export class AsideHostDirective {
  constructor(public viewContainerRef: ViewContainerRef) {}
}
