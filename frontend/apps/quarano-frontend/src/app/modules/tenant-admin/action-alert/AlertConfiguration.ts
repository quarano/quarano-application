import {Alert} from '../../../models/action';

export interface AlertConfiguration {
  alert: Alert;
  color: string;
  icon: string;
  displayName: string;
  order: number;
}

export function alertConfigurations(): AlertConfiguration[] {
  return [
    {
      alert: Alert.DIARY_ENTRY_MISSING,
      color: 'aquamarine',
      icon: 'create',
      displayName: 'Fehlender Eintrag',
      order: 0
    },
    {
      alert: Alert.ENCOUNTER_IN_QUARANTINE,
      color: 'antiquewhite',
      icon: 'group',
      displayName: 'Kontakt in Quarantäne',
      order: 1
    },
    {
      alert: Alert.FIRST_CHARACTERISTIC_SYMPTOM,
      color: 'burlywood',
      icon: 'healing',
      displayName: 'Erste Symptome',
      order: 2
    },
    {
      alert: Alert.INCREASED_TEMPERATURE,
      color: 'lightpink',
      icon: 'trending_up',
      displayName: 'Symptome auffällig',
      order: 3
    },
    {
      alert: Alert.INITIAL_CALL_OPEN_INDEX,
      color: 'lightblue',
      icon: 'local_phone',
      displayName: 'Initialer Anruf offen (Index)',
      order: 4
    },
    {
      alert: Alert.INITIAL_CALL_OPEN_CONTACT,
      color: 'lightblue',
      icon: 'local_phone',
      displayName: 'Initialer Anruf offen (KP)',
      order: 5
    },
    {
      alert: Alert.MISSING_DETAILS_INDEX,
      color: 'darkkhaki',
      icon: 'device_unknown',
      displayName: 'Recherche notwendig (Index)',
      order: 6
    },
    {
      alert: Alert.MISSING_DETAILS_CONTACT,
      color: 'darkkhaki',
      icon: 'device_unknown',
      displayName: 'Recherche notwendig (KP)',
      order: 7
    },
    {
      alert: Alert.QUARANTINE_END,
      color: 'palegreen',
      icon: 'event_available',
      displayName: 'Quarantäne Ende prüfen',
      order: 8
    }
  ];
}
