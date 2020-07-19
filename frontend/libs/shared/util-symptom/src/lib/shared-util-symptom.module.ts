import { SymptomDataService } from './data-access/symptom-data.service';
import { SymptomEntityService, SYMPTOM_FEATURE_KEY } from './data-access/symptom-entity.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SymptomsResolver } from './resolvers/symptoms.resolver';
import { EntityMetadataMap, EntityDefinitionService, EntityDataService } from '@ngrx/data';

const entityMetadata: EntityMetadataMap = {
  Symptom: {},
};

@NgModule({
  imports: [CommonModule],
  providers: [SymptomEntityService, SymptomDataService, SymptomsResolver],
})
export class SharedUtilSymptomModule {
  constructor(
    private eds: EntityDefinitionService,
    private entityDataService: EntityDataService,
    private symptomDataService: SymptomDataService
  ) {
    eds.registerMetadataMap(entityMetadata);
    entityDataService.registerService(SYMPTOM_FEATURE_KEY, symptomDataService);
  }
}
