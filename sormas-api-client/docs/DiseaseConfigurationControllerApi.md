# DiseaseConfigurationControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                              | HTTP request                               | Description |
| --------------------------------------------------------------------------------------------------- | ------------------------------------------ | ----------- |
| [**getAllDiseaseConfigurations**](DiseaseConfigurationControllerApi.md#getAllDiseaseConfigurations) | **GET** /diseaseconfigurations/all/{since} |
| [**getAllUuids3**](DiseaseConfigurationControllerApi.md#getAllUuids3)                               | **GET** /diseaseconfigurations/uuids       |
| [**getByUuids6**](DiseaseConfigurationControllerApi.md#getByUuids6)                                 | **POST** /diseaseconfigurations/query      |

<a name="getAllDiseaseConfigurations"></a>

# **getAllDiseaseConfigurations**

> List&lt;DiseaseConfigurationDto&gt; getAllDiseaseConfigurations(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
Long since = 789L; // Long |
try {
    List<DiseaseConfigurationDto> result = apiInstance.getAllDiseaseConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllDiseaseConfigurations");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids3"></a>

# **getAllUuids3**

> List&lt;String&gt; getAllUuids3()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids3();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllUuids3");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids6"></a>

# **getByUuids6**

> List&lt;DiseaseConfigurationDto&gt; getByUuids6(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<DiseaseConfigurationDto> result = apiInstance.getByUuids6(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getByUuids6");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
