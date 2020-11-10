import { ClientStore } from './store/client-store.service';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { clientFeatureKey, clientReducer } from './reducers';
import { EffectsModule } from '@ngrx/effects';
import { ClientEffects } from './store/client.effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(clientFeatureKey, clientReducer),
    EffectsModule.forFeature([ClientEffects]),
  ],
  providers: [ClientStore],
})
export class ClientStoreModule {
  static forRoot(): ModuleWithProviders<ClientStoreModule> {
    return {
      ngModule: ClientStoreModule,
      providers: [ClientStore],
    };
  }
}
