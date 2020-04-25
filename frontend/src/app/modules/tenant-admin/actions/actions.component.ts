import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { ActionListItemDto } from '@models/action';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.css']
})
export class ActionsComponent implements OnInit, OnDestroy {
  actions: ActionListItemDto[] = [];
  private subs = new SubSink();
  rows = [
    { name: 'Austin', gender: 'Male', company: 'Swimlane' },
    { name: 'Dany', gender: 'Male', company: 'KFC' },
    { name: 'Molly', gender: 'Female', company: 'Burger King' }
  ];
  columns = [{ prop: 'name' }, { name: 'Gender' }, { name: 'Company' }];

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.actions = data.actions;
    }));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }



}
