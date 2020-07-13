# EpiDataDto

## Properties

| Name                                    | Type                                                          | Description | Notes      |
| --------------------------------------- | ------------------------------------------------------------- | ----------- | ---------- |
| **creationDate**                        | [**OffsetDateTime**](OffsetDateTime.md)                       |             | [optional] |
| **changeDate**                          | [**OffsetDateTime**](OffsetDateTime.md)                       |             | [optional] |
| **uuid**                                | **String**                                                    |             | [optional] |
| **burialAttended**                      | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **gatheringAttended**                   | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **traveled**                            | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **burials**                             | [**List&lt;EpiDataBurialDto&gt;**](EpiDataBurialDto.md)       |             | [optional] |
| **gatherings**                          | [**List&lt;EpiDataGatheringDto&gt;**](EpiDataGatheringDto.md) |             | [optional] |
| **travels**                             | [**List&lt;EpiDataTravelDto&gt;**](EpiDataTravelDto.md)       |             | [optional] |
| **directContactConfirmedCase**          | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **directContactProbableCase**           | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **closeContactProbableCase**            | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **areaConfirmedCases**                  | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **processingConfirmedCaseFluidUnsafe**  | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **percutaneousCaseBlood**               | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **directContactDeadUnsafe**             | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **processingSuspectedCaseSampleUnsafe** | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **areaInfectedAnimals**                 | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **sickDeadAnimals**                     | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **sickDeadAnimalsDetails**              | **String**                                                    |             | [optional] |
| **sickDeadAnimalsDate**                 | [**OffsetDateTime**](OffsetDateTime.md)                       |             | [optional] |
| **sickDeadAnimalsLocation**             | **String**                                                    |             | [optional] |
| **eatingRawAnimalsInInfectedArea**      | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **eatingRawAnimals**                    | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **eatingRawAnimalsDetails**             | **String**                                                    |             | [optional] |
| **rodents**                             | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **bats**                                | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **primates**                            | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **swine**                               | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **birds**                               | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **rabbits**                             | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **cattle**                              | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **dogs**                                | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **cats**                                | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **canidae**                             | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **otherAnimals**                        | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **otherAnimalsDetails**                 | **String**                                                    |             | [optional] |
| **waterSource**                         | [**WaterSource**](WaterSource.md)                             |             | [optional] |
| **waterSourceOther**                    | **String**                                                    |             | [optional] |
| **waterBody**                           | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **waterBodyDetails**                    | **String**                                                    |             | [optional] |
| **tickBite**                            | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **fleaBite**                            | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureBite**                  | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureTouch**                 | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureScratch**               | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureLick**                  | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureOther**                 | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **kindOfExposureDetails**               | **String**                                                    |             | [optional] |
| **dateOfLastExposure**                  | [**OffsetDateTime**](OffsetDateTime.md)                       |             | [optional] |
| **placeOfLastExposure**                 | **String**                                                    |             | [optional] |
| **animalCondition**                     | [**AnimalCondition**](AnimalCondition.md)                     |             | [optional] |
| **animalVaccinationStatus**             | [**Vaccination**](Vaccination.md)                             |             | [optional] |
| **prophylaxisStatus**                   | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **dateOfProphylaxis**                   | [**OffsetDateTime**](OffsetDateTime.md)                       |             | [optional] |
| **visitedHealthFacility**               | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **contactWithSourceRespiratoryCase**    | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **visitedAnimalMarket**                 | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **camels**                              | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
| **snakes**                              | [**YesNoUnknown**](YesNoUnknown.md)                           |             | [optional] |
