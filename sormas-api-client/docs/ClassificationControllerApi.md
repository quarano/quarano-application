# ClassificationControllerApi

All URIs are relative to _/sormas-rest_

| Method                                              | HTTP request                        | Description |
| --------------------------------------------------- | ----------------------------------- | ----------- |
| [**getAll**](ClassificationControllerApi.md#getAll) | **GET** /classification/all/{since} |

<a name="getAll"></a>

# **getAll**

> List&lt;DiseaseClassificationCriteriaDto&gt; getAll(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
Long since = 789L; // Long |
try {
    List<DiseaseClassificationCriteriaDto> result = apiInstance.getAll(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#getAll");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;DiseaseClassificationCriteriaDto&gt;**](DiseaseClassificationCriteriaDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8
