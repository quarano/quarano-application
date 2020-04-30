import { Alert } from '@models/action';
import { Component, OnInit, Input } from '@angular/core';

export interface AlertConfiguration {
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
        displayName: 'Erste Symptome'
      },
      {
        alert: Alert.INCREASED_TEMPERATURE,
        color: 'lightpink',
        icon: 'trending_up',
        displayName: 'Symptome auffällig'
      },
      {
        alert: Alert.INITIAL_CALL_OPEN_CONTACT,
        color: 'lightblue',
        icon: 'local_phone',
        displayName: 'Initialer Anruf offen'
      },
      {
        alert: Alert.INITIAL_CALL_OPEN_INDEX,
        color: 'lightblue',
        icon: 'local_phone',
        displayName: 'Initialer Anruf offen'
      },
      {
        alert: Alert.MISSING_DETAILS_CONTACT,
        color: 'darkkhaki',
        icon: 'device_unknown',
        displayName: 'Recherche notwendig'
      },
      {
        alert: Alert.MISSING_DETAILS_INDEX,
        color: 'darkkhaki',
        icon: 'device_unknown',
        displayName: 'Recherche notwendig'
      },
      {
        alert: Alert.QUARANTINE_END,
        color: 'palegreen',
        icon: 'event_available',
        displayName: 'Quarantäne Ende prüfen'
      }
    ];
  }
}
