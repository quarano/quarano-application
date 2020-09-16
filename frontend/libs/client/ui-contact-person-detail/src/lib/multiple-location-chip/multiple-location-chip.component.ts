import { Component, OnInit } from '@angular/core';
import { ContactDialogService } from '../services/contact-dialog.service';
import { StoredContactLocationDto } from '@qro/client/domain';

@Component({
  selector: 'qro-multiple-location-chip',
  templateUrl: './multiple-location-chip.component.html',
  styleUrls: ['./multiple-location-chip.component.scss'],
})
export class MultipleLocationChipComponent implements OnInit {
  visible = true;
  selectable = true;
  removable = true;
  addedLocations: StoredContactLocationDto[] = [];

  constructor(private dialogService: ContactDialogService) {}

  ngOnInit(): void {}

  openLocationDialog() {
    this.dialogService
      .openContactLocationDialog({}) // Empty config opens a blank form
      .afterClosed()
      .subscribe((newLocation) => {
        if (newLocation && newLocation.id) {
          this.addedLocations.push(newLocation);
        }
      });
  }

  remove(location: StoredContactLocationDto) {
    this.addedLocations = this.addedLocations.filter((dto) => dto.id !== location.id);
  }

  getLocationTitle(location: StoredContactLocationDto): string {
    if (!location) {
      return '';
    }
    if (location.name) {
      return location.name;
    } else {
      return `${location.street} ${location.houseNumber}`;
    }
  }
}
