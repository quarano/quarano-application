# SampleDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**associatedCase** | [**CaseReferenceDto**](CaseReferenceDto.md) |  |  [optional]
**associatedContact** | [**ContactReferenceDto**](ContactReferenceDto.md) |  |  [optional]
**associatedEventParticipant** | [**EventParticipantReferenceDto**](EventParticipantReferenceDto.md) |  |  [optional]
**labSampleID** | **String** |  |  [optional]
**fieldSampleID** | **String** |  |  [optional]
**sampleDateTime** | [**OffsetDateTime**](OffsetDateTime.md) |  | 
**reportDateTime** | [**OffsetDateTime**](OffsetDateTime.md) |  | 
**reportingUser** | [**UserReferenceDto**](UserReferenceDto.md) |  | 
**reportLat** | **Double** |  |  [optional]
**reportLon** | **Double** |  |  [optional]
**reportLatLonAccuracy** | **Float** |  |  [optional]
**sampleMaterial** | [**SampleMaterial**](SampleMaterial.md) |  | 
**sampleMaterialText** | **String** |  |  [optional]
**samplePurpose** | [**SamplePurpose**](SamplePurpose.md) |  | 
**lab** | [**FacilityReferenceDto**](FacilityReferenceDto.md) |  | 
**labDetails** | **String** |  |  [optional]
**shipmentDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**shipmentDetails** | **String** |  |  [optional]
**receivedDate** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**specimenCondition** | [**SpecimenCondition**](SpecimenCondition.md) |  |  [optional]
**noTestPossibleReason** | **String** |  |  [optional]
**comment** | **String** |  |  [optional]
**sampleSource** | [**SampleSource**](SampleSource.md) |  |  [optional]
**referredTo** | [**SampleReferenceDto**](SampleReferenceDto.md) |  |  [optional]
**shipped** | **Boolean** |  |  [optional]
**received** | **Boolean** |  |  [optional]
**pathogenTestResult** | [**PathogenTestResultType**](PathogenTestResultType.md) |  |  [optional]
**pathogenTestingRequested** | **Boolean** |  |  [optional]
**additionalTestingRequested** | **Boolean** |  |  [optional]
**requestedPathogenTests** | [**List&lt;PathogenTestType&gt;**](PathogenTestType.md) |  |  [optional]
**requestedAdditionalTests** | [**List&lt;AdditionalTestType&gt;**](AdditionalTestType.md) |  |  [optional]
**requestedOtherPathogenTests** | **String** |  |  [optional]
**requestedOtherAdditionalTests** | **String** |  |  [optional]
**sormasToSormasOriginInfo** | [**SormasToSormasOriginInfoDto**](SormasToSormasOriginInfoDto.md) |  |  [optional]
**ownershipHandedOver** | **Boolean** |  |  [optional]
