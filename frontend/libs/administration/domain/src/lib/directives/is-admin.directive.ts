import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';
import { UserService } from '../../../../../auth/domain/src/lib/services/user.service';

@Directive({
  selector: '[qroIsAdmin]'
})
export class IsAdminDirective implements OnInit {
  @Input() qroIsAdmin: boolean;
  isVisible = false;

  constructor(
    private viewContainerRef: ViewContainerRef,
    private templateRef: TemplateRef<any>,
    private userService: UserService) { }

  ngOnInit() {
    if (this.userService.isAdmin) {
      if (!this.isVisible) {
        this.isVisible = true;
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      }
    } else {
      this.isVisible = false;
      this.viewContainerRef.clear();
    }
  }

}
