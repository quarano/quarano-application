import { Injectable } from '@angular/core';
import { ClientType } from '@models/report-case';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor() { }

  getTypeName(clientType: ClientType): string {
    switch (clientType) {
      case ClientType.Contact:
        return 'Kontakt';
      case ClientType.Index:
        return 'Index';
      default:
        return '-';
    }
  }

}
