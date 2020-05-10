import { IIdentifiable } from '@models/general';
import { FormControl } from '@angular/forms';
import { Component, OnInit, Input, ElementRef, ViewChild, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { map, startWith, takeUntil } from 'rxjs/operators';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-multiple-autocomplete',
  templateUrl: './multiple-autocomplete.component.html',
  styleUrls: ['./multiple-autocomplete.component.scss']
})
export class MultipleAutocompleteComponent implements OnInit, OnDestroy {
  @Input() nameProperties: string[];
  @Input() control: FormControl;
  @Input() placeholder: string;
  @Input() selectableItems: IIdentifiable[];
  @Output() removed = new EventEmitter<string>();
  @Output() added = new EventEmitter<string>();
  filteredItems: Observable<IIdentifiable[]>;
  selectedItemIds: string[];
  inputControl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  @ViewChild('input') input: ElementRef<HTMLInputElement>;

  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.control.valueChanges.pipe(
      takeUntil(this.destroy$)
    ).subscribe((data: string[]) => {
      this.selectedItemIds = data;
      this.control.markAsDirty();
      data.forEach(value => this.added.emit(value));
    });

    this.selectedItemIds = this.control.value;

    this.filteredItems = this.inputControl.valueChanges.pipe(
      takeUntil(this.destroy$),
      startWith(null as string),
      map((searchTerm: number | string | null) => {
        return typeof (searchTerm) === 'string' ? this._filter(searchTerm) : this.selectableItems.slice();
      }));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private _filter(searchTerm: string): IIdentifiable[] {
    const arrayToReturn = this.selectableItems.filter(item => !this.selectedItemIds.includes(item.id));

    if (!searchTerm) { return arrayToReturn; }
    const filterValue = searchTerm.toLowerCase();

    return arrayToReturn.filter(item => {
      return this.getName(item).toLowerCase().indexOf(filterValue) === 0;
    });
  }

  get disabled(): boolean {
    return this.control.disabled;
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const selectedValue = event.option.value;
    this.selectedItemIds.push(selectedValue);
    this.input.nativeElement.value = '';
    this.inputControl.setValue(null);
    this.setFormControlValue();
    this.added.emit(selectedValue);
  }

  remove(id: string): void {
    const index = this.selectedItemIds.indexOf(id);

    if (index >= 0) {
      this.selectedItemIds.splice(index, 1);
      this.setFormControlValue();
      this.removed.emit(id);
    }
  }

  setFormControlValue() {
    this.control.setValue(this.selectedItemIds);
    this.control.markAsDirty();
  }

  getNameById(id: string) {
    const item = this.selectableItems.find(i => i.id === id);
    return this.getName(item);
  }

  getName(item: IIdentifiable) {
    if (!item) { return ''; }
    let name = '';
    this.nameProperties.forEach(prop => {
      name += item[prop] + ' ';
    });
    return name.substring(0, name.length - 1);
  }
}
