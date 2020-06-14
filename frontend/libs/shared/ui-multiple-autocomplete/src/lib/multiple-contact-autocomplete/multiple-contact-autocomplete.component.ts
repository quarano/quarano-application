import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { IIdentifiable } from '../../../../../../apps/quarano-frontend/src/app/models/general';
import { MultipleAutocompleteComponent } from '../multiple-autocomplete/multiple-autocomplete.component';
import { QroDialogService } from '../../../../../../apps/quarano-frontend/src/app/services/qro-dialog.service';

@Component({
  selector: 'qro-multiple-contact-autocomplete',
  templateUrl: './multiple-contact-autocomplete.component.html',
  styleUrls: ['./multiple-contact-autocomplete.component.css'],
})
export class MultipleContactAutocompleteComponent {
  @Input() nameProperties: string[];
  @Input() control: FormControl;
  @Input() placeholder: string;
  @Input() selectableItems: IIdentifiable[];
  @Output() removed = new EventEmitter<string>();
  @Output() added = new EventEmitter<string>();
  @Output() itemNotFound = new EventEmitter<string>();

  @ViewChild('contactMultipleAutoComplete')
  contactMultipleAutocomplete: MultipleAutocompleteComponent;

  constructor(private dialogService: QroDialogService) {}

  addMissingContactPerson(name: string) {
    this.dialogService.askAndOpenContactPersonDialog(name).subscribe((createdContact) => {
      this.contactMultipleAutocomplete.clearInput();
      this.selectableItems.push(createdContact);
      this.control.patchValue([...this.control.value, createdContact.id]);
    });
  }

  emitItemNotFound(event: any) {
    this.itemNotFound.emit(event);
  }

  emitAdded(event: any) {
    this.added.emit(event);
  }

  emitRemoved(event: any) {
    this.itemNotFound.emit(event);
  }
}
