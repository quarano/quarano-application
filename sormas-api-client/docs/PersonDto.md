# PersonDto

## Properties

| Name                            | Type                                                  | Description | Notes      |
| ------------------------------- | ----------------------------------------------------- | ----------- | ---------- |
| **creationDate**                | [**OffsetDateTime**](OffsetDateTime.md)               |             | [optional] |
| **changeDate**                  | [**OffsetDateTime**](OffsetDateTime.md)               |             | [optional] |
| **uuid**                        | **String**                                            |             | [optional] |
| **pseudonymized**               | **Boolean**                                           |             | [optional] |
| **firstName**                   | **String**                                            |             |
| **lastName**                    | **String**                                            |             | [optional] |
| **nickname**                    | **String**                                            |             | [optional] |
| **mothersName**                 | **String**                                            |             | [optional] |
| **mothersMaidenName**           | **String**                                            |             | [optional] |
| **fathersName**                 | **String**                                            |             | [optional] |
| **sex**                         | [**Sex**](Sex.md)                                     |             | [optional] |
| **birthdateDD**                 | **Integer**                                           |             | [optional] |
| **birthdateMM**                 | **Integer**                                           |             | [optional] |
| **birthdateYYYY**               | **Integer**                                           |             | [optional] |
| **approximateAge**              | **Integer**                                           |             | [optional] |
| **approximateAgeType**          | [**ApproximateAgeType**](ApproximateAgeType.md)       |             | [optional] |
| **approximateAgeReferenceDate** | [**OffsetDateTime**](OffsetDateTime.md)               |             | [optional] |
| **placeOfBirthRegion**          | [**RegionReferenceDto**](RegionReferenceDto.md)       |             | [optional] |
| **placeOfBirthDistrict**        | [**DistrictReferenceDto**](DistrictReferenceDto.md)   |             | [optional] |
| **placeOfBirthCommunity**       | [**CommunityReferenceDto**](CommunityReferenceDto.md) |             | [optional] |
| **placeOfBirthFacility**        | [**FacilityReferenceDto**](FacilityReferenceDto.md)   |             | [optional] |
| **placeOfBirthFacilityDetails** | **String**                                            |             | [optional] |
| **gestationAgeAtBirth**         | **Integer**                                           |             | [optional] |
| **birthWeight**                 | **Integer**                                           |             | [optional] |
| **presentCondition**            | [**PresentCondition**](PresentCondition.md)           |             | [optional] |
| **deathDate**                   | [**OffsetDateTime**](OffsetDateTime.md)               |             | [optional] |
| **causeOfDeath**                | [**CauseOfDeath**](CauseOfDeath.md)                   |             | [optional] |
| **causeOfDeathDisease**         | [**Disease**](Disease.md)                             |             | [optional] |
| **causeOfDeathDetails**         | **String**                                            |             | [optional] |
| **deathPlaceType**              | [**DeathPlaceType**](DeathPlaceType.md)               |             | [optional] |
| **deathPlaceDescription**       | **String**                                            |             | [optional] |
| **burialDate**                  | [**OffsetDateTime**](OffsetDateTime.md)               |             | [optional] |
| **burialPlaceDescription**      | **String**                                            |             | [optional] |
| **burialConductor**             | [**BurialConductor**](BurialConductor.md)             |             | [optional] |
| **phone**                       | **String**                                            |             | [optional] |
| **phoneOwner**                  | **String**                                            |             | [optional] |
| **address**                     | [**LocationDto**](LocationDto.md)                     |             | [optional] |
| **emailAddress**                | **String**                                            |             | [optional] |
| **educationType**               | [**EducationType**](EducationType.md)                 |             | [optional] |
| **educationDetails**            | **String**                                            |             | [optional] |
| **occupationType**              | [**OccupationType**](OccupationType.md)               |             | [optional] |
| **occupationDetails**           | **String**                                            |             | [optional] |
| **occupationRegion**            | [**RegionReferenceDto**](RegionReferenceDto.md)       |             | [optional] |
| **occupationDistrict**          | [**DistrictReferenceDto**](DistrictReferenceDto.md)   |             | [optional] |
| **occupationCommunity**         | [**CommunityReferenceDto**](CommunityReferenceDto.md) |             | [optional] |
| **occupationFacility**          | [**FacilityReferenceDto**](FacilityReferenceDto.md)   |             | [optional] |
| **occupationFacilityDetails**   | **String**                                            |             | [optional] |
| **generalPractitionerDetails**  | **String**                                            |             | [optional] |
| **passportNumber**              | **String**                                            |             | [optional] |
| **nationalHealthId**            | **String**                                            |             | [optional] |
