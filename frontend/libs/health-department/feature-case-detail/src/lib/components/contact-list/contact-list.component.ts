import { Router } from '@angular/router';
import { Component, OnInit, Input } from '@angular/core';
import { ContactListItemDto } from '@qro/health-department/domain';

interface RowViewModel {
  firstName: string;
  lastName: string;
  isHealthStaff: string;
  isSenior: string;
  hasPreExistingConditions: string;
  lastContact: Date;
  status: string;
}

@Component({
  selector: 'qro-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.scss'],
})
export class ContactListComponent implements OnInit {
  @Input() contacts: ContactListItemDto[];
  @Input() caseName: string;

  rows: RowViewModel[] = [];

  constructor(private router: Router) {}

  ngOnInit() {
    this.rows = this.contacts.map((c) => this.getRowData(c));
  }

  onSelect(event) {
    // ToDo: Anpassen, wenn type und id des Falls mitgeliefert werden.
    // Snackbar anzeigen, wenn kein Fall existiert
    this.router.navigate(['/health-department/case-detail', event?.selected[0]?.type, event?.selected[0]?.contactId]);
  }

  private getRowData(listItem: ContactListItemDto): RowViewModel {
    return {
      firstName: listItem.firstName,
      lastName: listItem.lastName,
      status: listItem.contactStatus,
      isHealthStaff: this.getBooleanText(listItem.isHealthStaff),
      isSenior: this.getBooleanText(listItem.isSenior),
      hasPreExistingConditions: this.getBooleanText(listItem.hasPreExistingConditions),
      lastContact:
        listItem.descendingSortedContactDates.length > 0 ? new Date(listItem.descendingSortedContactDates[0]) : null,
    };
  }

  private getBooleanText(value: boolean): string {
    if (value) {
      return 'ja';
    }
    if (value === false) {
      return 'nein';
    }
    return '?';
  }
}
