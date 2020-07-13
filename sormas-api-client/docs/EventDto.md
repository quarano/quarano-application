# EventDto

## Properties

| Name                     | Type                                        | Description | Notes      |
| ------------------------ | ------------------------------------------- | ----------- | ---------- |
| **creationDate**         | [**OffsetDateTime**](OffsetDateTime.md)     |             | [optional] |
| **changeDate**           | [**OffsetDateTime**](OffsetDateTime.md)     |             | [optional] |
| **uuid**                 | **String**                                  |             | [optional] |
| **eventStatus**          | [**EventStatus**](EventStatus.md)           |             |
| **eventDesc**            | **String**                                  |             |
| **eventDate**            | [**OffsetDateTime**](OffsetDateTime.md)     |             | [optional] |
| **reportDateTime**       | [**OffsetDateTime**](OffsetDateTime.md)     |             |
| **reportingUser**        | [**UserReferenceDto**](UserReferenceDto.md) |             |
| **eventLocation**        | [**LocationDto**](LocationDto.md)           |             | [optional] |
| **typeOfPlace**          | [**TypeOfPlace**](TypeOfPlace.md)           |             | [optional] |
| **srcFirstName**         | **String**                                  |             | [optional] |
| **srcLastName**          | **String**                                  |             | [optional] |
| **srcTelNo**             | **String**                                  |             | [optional] |
| **srcEmail**             | **String**                                  |             | [optional] |
| **disease**              | [**Disease**](Disease.md)                   |             | [optional] |
| **diseaseDetails**       | **String**                                  |             | [optional] |
| **surveillanceOfficer**  | [**UserReferenceDto**](UserReferenceDto.md) |             | [optional] |
| **typeOfPlaceText**      | **String**                                  |             | [optional] |
| **reportLat**            | **Double**                                  |             | [optional] |
| **reportLon**            | **Double**                                  |             | [optional] |
| **reportLatLonAccuracy** | **Float**                                   |             | [optional] |
