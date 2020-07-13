# HospitalizationDto

## Properties

| Name                         | Type                                                                        | Description | Notes      |
| ---------------------------- | --------------------------------------------------------------------------- | ----------- | ---------- |
| **creationDate**             | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **changeDate**               | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **uuid**                     | **String**                                                                  |             | [optional] |
| **admittedToHealthFacility** | [**YesNoUnknown**](YesNoUnknown.md)                                         |             | [optional] |
| **admissionDate**            | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **dischargeDate**            | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **isolated**                 | [**YesNoUnknown**](YesNoUnknown.md)                                         |             | [optional] |
| **isolationDate**            | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **leftAgainstAdvice**        | [**YesNoUnknown**](YesNoUnknown.md)                                         |             | [optional] |
| **hospitalizedPreviously**   | [**YesNoUnknown**](YesNoUnknown.md)                                         |             | [optional] |
| **previousHospitalizations** | [**List&lt;PreviousHospitalizationDto&gt;**](PreviousHospitalizationDto.md) |             | [optional] |
| **intensiveCareUnit**        | [**YesNoUnknown**](YesNoUnknown.md)                                         |             | [optional] |
| **intensiveCareUnitStart**   | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
| **intensiveCareUnitEnd**     | [**OffsetDateTime**](OffsetDateTime.md)                                     |             | [optional] |
