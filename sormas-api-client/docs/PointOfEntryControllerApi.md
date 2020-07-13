# PointOfEntryControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                        | HTTP request                       | Description |
| ------------------------------------------------------------- | ---------------------------------- | ----------- |
| [**getAll3**](PointOfEntryControllerApi.md#getAll3)           | **GET** /pointsofentry/all/{since} |
| [**getAllUuids8**](PointOfEntryControllerApi.md#getAllUuids8) | **GET** /pointsofentry/uuids       |
| [**getByUuids14**](PointOfEntryControllerApi.md#getByUuids14) | **POST** /pointsofentry/query      |

<a name="getAll3"></a>

# **getAll3**

> List&lt;PointOfEntryDto&gt; getAll3(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
Long since = 789L; // Long |
try {
    List<PointOfEntryDto> result = apiInstance.getAll3(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAll3");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids8"></a>

# **getAllUuids8**

> List&lt;String&gt; getAllUuids8()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
try {
    List<String> result = apiInstance.getAllUuids8();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAllUuids8");
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

<a name="getByUuids14"></a>

# **getByUuids14**

> List&lt;PointOfEntryDto&gt; getByUuids14(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<PointOfEntryDto> result = apiInstance.getByUuids14(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getByUuids14");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
