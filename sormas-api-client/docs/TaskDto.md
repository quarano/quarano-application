# TaskDto

## Properties

| Name                     | Type                                              | Description | Notes      |
| ------------------------ | ------------------------------------------------- | ----------- | ---------- |
| **creationDate**         | [**OffsetDateTime**](OffsetDateTime.md)           |             | [optional] |
| **changeDate**           | [**OffsetDateTime**](OffsetDateTime.md)           |             | [optional] |
| **uuid**                 | **String**                                        |             | [optional] |
| **taskContext**          | [**TaskContext**](TaskContext.md)                 |             |
| **caze**                 | [**CaseReferenceDto**](CaseReferenceDto.md)       |             | [optional] |
| **event**                | [**EventReferenceDto**](EventReferenceDto.md)     |             | [optional] |
| **contact**              | [**ContactReferenceDto**](ContactReferenceDto.md) |             | [optional] |
| **taskType**             | [**TaskType**](TaskType.md)                       |             |
| **priority**             | [**TaskPriority**](TaskPriority.md)               |             | [optional] |
| **dueDate**              | [**OffsetDateTime**](OffsetDateTime.md)           |             |
| **suggestedStart**       | [**OffsetDateTime**](OffsetDateTime.md)           |             | [optional] |
| **taskStatus**           | [**TaskStatus**](TaskStatus.md)                   |             | [optional] |
| **statusChangeDate**     | [**OffsetDateTime**](OffsetDateTime.md)           |             | [optional] |
| **perceivedStart**       | [**OffsetDateTime**](OffsetDateTime.md)           |             | [optional] |
| **creatorUser**          | [**UserReferenceDto**](UserReferenceDto.md)       |             | [optional] |
| **creatorComment**       | **String**                                        |             | [optional] |
| **assigneeUser**         | [**UserReferenceDto**](UserReferenceDto.md)       |             |
| **assigneeReply**        | **String**                                        |             | [optional] |
| **closedLat**            | **Double**                                        |             | [optional] |
| **closedLon**            | **Double**                                        |             | [optional] |
| **closedLatLonAccuracy** | **Float**                                         |             | [optional] |
| **contextReference**     | [**ReferenceDto**](ReferenceDto.md)               |             | [optional] |
