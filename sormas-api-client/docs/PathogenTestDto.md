# PathogenTestDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**sample** | [**SampleReferenceDto**](SampleReferenceDto.md) |  | 
**testedDisease** | [**Disease**](Disease.md) |  | 
**testedDiseaseDetails** | **String** |  |  [optional]
**testType** | [**PathogenTestType**](PathogenTestType.md) |  | 
**testTypeText** | **String** |  |  [optional]
**testDateTime** | [**OffsetDateTime**](OffsetDateTime.md) |  | 
**lab** | [**FacilityReferenceDto**](FacilityReferenceDto.md) |  | 
**labDetails** | **String** |  |  [optional]
**labUser** | [**UserReferenceDto**](UserReferenceDto.md) |  | 
**testResult** | [**PathogenTestResultType**](PathogenTestResultType.md) |  | 
**testResultText** | **String** |  | 
**testResultVerified** | **Boolean** |  | 
**fourFoldIncreaseAntibodyTiter** | **Boolean** |  |  [optional]
**serotype** | **String** |  |  [optional]
**cqValue** | **Float** |  |  [optional]
