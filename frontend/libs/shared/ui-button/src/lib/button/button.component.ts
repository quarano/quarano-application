import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'qro-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() color = 'primary';
  @Input() disabled: boolean;
  @Input() type = 'submit';
  @Output() buttonClicked = new EventEmitter<any>();

  loading = false;

  save(): void {
    this.loading = true;
    this.buttonClicked.emit();
  }

}
