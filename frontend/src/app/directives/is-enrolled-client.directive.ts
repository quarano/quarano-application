import { EnrollmentService } from '@services/enrollment.service';
import { Directive, Input, ViewContainerRef, TemplateRef, OnInit } from '@angular/core';
import { UserService } from '@services/user.service';

@Directive({
  selector: '[appIsEnrolledClient]'
})
export class IsEnrolledClientDirective implements OnInit {
  @Input() appIsEnrolledClient: boolean;
  isVisible = false;

  constructor(
    private viewContainerRef: ViewContainerRef,
    private templateRef: TemplateRef<any>,
    private userService: UserService,
    private enrollmentService: EnrollmentService) { }

  ngOnInit() {
    if (this.userService.isHealthDepartmentUser) {
      this.setInivisibility();
      return;
    }

    this.enrollmentService.getEnrollmentStatus()
      .subscribe(status => {
        if (status?.complete) {
          if (!this.isVisible) {
            this.isVisible = true;
            this.viewContainerRef.createEmbeddedView(this.templateRef);
          }
        } else {
          this.setInivisibility();
        }
      });
  }

  private setInivisibility() {
    this.isVisible = false;
    this.viewContainerRef.clear();

  }
}
