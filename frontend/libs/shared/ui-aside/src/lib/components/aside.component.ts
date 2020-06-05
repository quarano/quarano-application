import { Component, OnInit, ViewChild, Type, ComponentFactoryResolver } from '@angular/core';
import { AsideHostDirective } from '../directives/aside-host.directive';
import { AsideService } from '../services/aside.service';

@Component({
  selector: 'qro-aside',
  template: `
                <ng-template qroAsideHost></ng-template>
            `,
  styleUrls: ['./aside.component.scss']
})
export class AsideComponent implements OnInit {
  @ViewChild(AsideHostDirective, { static: true }) asideHost: AsideHostDirective;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private asideService: AsideService) { }

  ngOnInit() {
    this.asideService.asideComponentContent$
      .subscribe(content => {
        this.showContent(content);
      });
  }

  showContent(contentComponent: Type<any>) {
    const viewContainerRef = this.asideHost.viewContainerRef;
    viewContainerRef.clear();

    if (contentComponent) {
      const componentFactory = this.componentFactoryResolver.resolveComponentFactory(contentComponent);
      viewContainerRef.createComponent(componentFactory);
    }
  }
}
