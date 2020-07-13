# FeatureConfigurationControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                              | HTTP request                                   | Description |
| --------------------------------------------------------------------------------------------------- | ---------------------------------------------- | ----------- |
| [**getAllFeatureConfigurations**](FeatureConfigurationControllerApi.md#getAllFeatureConfigurations) | **GET** /featureconfigurations/all/{since}     |
| [**getAllUuids6**](FeatureConfigurationControllerApi.md#getAllUuids6)                               | **GET** /featureconfigurations/uuids           |
| [**getByUuids11**](FeatureConfigurationControllerApi.md#getByUuids11)                               | **POST** /featureconfigurations/query          |
| [**getDeletedUuids**](FeatureConfigurationControllerApi.md#getDeletedUuids)                         | **GET** /featureconfigurations/deleted/{since} |

<a name="getAllFeatureConfigurations"></a>

# **getAllFeatureConfigurations**

> List&lt;FeatureConfigurationDto&gt; getAllFeatureConfigurations(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long |
try {
    List<FeatureConfigurationDto> result = apiInstance.getAllFeatureConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllFeatureConfigurations");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids6"></a>

# **getAllUuids6**

> List&lt;String&gt; getAllUuids6()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids6();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllUuids6");
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

<a name="getByUuids11"></a>

# **getByUuids11**

> List&lt;FeatureConfigurationDto&gt; getByUuids11(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<FeatureConfigurationDto> result = apiInstance.getByUuids11(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getByUuids11");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuids"></a>

# **getDeletedUuids**

> List&lt;String&gt; getDeletedUuids(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuids(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getDeletedUuids");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

**List&lt;String&gt;**

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8
