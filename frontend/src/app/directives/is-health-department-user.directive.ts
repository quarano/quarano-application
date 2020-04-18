import { UserService } from '@services/user.service';
import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';

@Directive({
  selector: '[appIsHealthDepartmentUser]'
})
export class IsHealthDepartmentUserDirective implements OnInit {
  @Input() appIsHealthDepartmentUser: boolean;
  isVisible = false;

  constructor(
    private viewContainerRef: ViewContainerRef,
    private templateRef: TemplateRef<any>,
    private userService: UserService) { }

  ngOnInit() {
    if (this.userService.isHealthDepartmentUser) {
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
