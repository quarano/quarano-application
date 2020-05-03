import { UserService } from '@services/user.service';
import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';

@Directive({
  selector: '[appIsAdmin]'
})
export class IsAdminDirective implements OnInit {
  @Input() appIsAdmin: boolean;
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
