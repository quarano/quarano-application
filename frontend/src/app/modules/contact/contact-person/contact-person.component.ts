import { SubSink } from 'subsink';
import {
  Component, OnInit, OnDestroy, HostListener
} from '@angular/core';

import {
  ActivatedRoute, Router
} from '@angular/router';
import { ContactPersonDto } from '@models/contact-person';
import { DeactivatableComponent } from '@guards/prevent-unsaved-changes.guard';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-contact-person',
  templateUrl: './contact-person.component.html',
  styleUrls: ['./contact-person.component.scss']
})
export class ContactPersonComponent implements OnInit, OnDestroy, DeactivatableComponent {
  private subs = new SubSink();
  contactPerson: ContactPersonDto;
  isDirty: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPerson = data.contactPerson;
    }));
  }

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return !this.isDirty;
  }

  setDirtyFlag(value: boolean) {
    this.isDirty = value;
  }

  navigateBack() {
    this.router.navigate(['/contacts']);
  }
}
