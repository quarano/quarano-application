import { AsideService } from '@services/aside.service';
import { AsideHostDirective } from '@directives/aside-host.directive';
import { Component, OnInit, ViewChild, Type, ComponentFactoryResolver } from '@angular/core';

@Component({
  selector: 'app-aside',
  template: `
                <ng-template appAsideHost></ng-template>
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
