import { Alert } from '@models/action';
import { Component, OnInit, Input } from '@angular/core';

interface AlertConfiguration {
  alert: Alert;
  color: string;
  icon: string;
  displayName: string;
}

@Component({
  selector: 'app-action-alert',
  templateUrl: './action-alert.component.html',
  styleUrls: ['./action-alert.component.scss']
})
export class ActionAlertComponent implements OnInit {
  @Input() alert: Alert;
  setting: AlertConfiguration;

  constructor() { }

  ngOnInit() {
    this.setting = this.config.find(c => c.alert === this.alert);
  }

  get config(): AlertConfiguration[] {
    return [
      {
        alert: Alert.DIARY_ENTRY_MISSING,
        color: 'aquamarine',
        icon: 'create',
        displayName: 'Fehlender Eintrag'
      },
      {
        alert: Alert.ENCOUNTER_IN_QUARANTINE,
        color: 'antiquewhite',
        icon: 'group',
        displayName: 'Kontakt in Quarantäne'
      },
      {
        alert: Alert.FIRST_CHARACTERISTIC_SYMPTOM,
        color: 'burlywood',
        icon: 'healing',
        displayName: 'Erstes charakteristisches Symptom'
      },
      {
        alert: Alert.INCREASED_TEMPERATURE,
        color: 'darkkhaki',
        icon: 'trending_up',
        displayName: 'Hohe Körpertemperatur'
      },
      {
        alert: Alert.INITIAL_CALL_OPEN_CONTACT,
        color: 'darksalmon',
        icon: 'group_add',
        displayName: 'Initialer Anruf Kontaktperson'
      },
      {
        alert: Alert.INITIAL_CALL_OPEN_INDEX,
        color: 'lavender',
        icon: 'person_add',
        displayName: 'Initialer Anruf Indexpatient'
      },
      {
        alert: Alert.MISSING_DETAILS_CONTACT,
        color: 'lightgoldenrodyellow',
        icon: 'device_unknown',
        displayName: 'Fehlende Stammdaten Kontaktperson'
      },
      {
        alert: Alert.MISSING_DETAILS_INDEX,
        color: 'lightpink',
        icon: 'not_listed_location',
        displayName: 'Fehlende Stammdaten Indexpatient'
      },
      {
        alert: Alert.QUARANTINE_END,
        color: 'palegreen',
        icon: 'event_available',
        displayName: 'Ende der Quarantäne'
      }
    ];
  }
}
