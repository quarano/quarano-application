import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'qro-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
})
export class ButtonComponent {
  @Input() color = 'primary';
  @Input() disabled = false;
  @Input() type = 'submit';
  @Input() loading = false;

  @Output() buttonClicked = new EventEmitter<any>();

  save(event: any): void {
    this.buttonClicked.emit(event);
  }
}
