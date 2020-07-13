# InfrastructureControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                    | HTTP request                  | Description |
| ----------------------------------------------------------------------------------------- | ----------------------------- | ----------- |
| [**getInfrastructureSyncData**](InfrastructureControllerApi.md#getInfrastructureSyncData) | **POST** /infrastructure/sync |

<a name="getInfrastructureSyncData"></a>

# **getInfrastructureSyncData**

> InfrastructureSyncDto getInfrastructureSyncData(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.InfrastructureControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

InfrastructureControllerApi apiInstance = new InfrastructureControllerApi();
InfrastructureChangeDatesDto body = new InfrastructureChangeDatesDto(); // InfrastructureChangeDatesDto |
try {
    InfrastructureSyncDto result = apiInstance.getInfrastructureSyncData(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfrastructureControllerApi#getInfrastructureSyncData");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                                | Description | Notes      |
| -------- | ------------------------------------------------------------------- | ----------- | ---------- |
| **body** | [**InfrastructureChangeDatesDto**](InfrastructureChangeDatesDto.md) |             | [optional] |

### Return type

[**InfrastructureSyncDto**](InfrastructureSyncDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
