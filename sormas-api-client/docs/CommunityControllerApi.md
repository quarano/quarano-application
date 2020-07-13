# CommunityControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                     | HTTP request                     | Description |
| ---------------------------------------------------------- | -------------------------------- | ----------- |
| [**getAll1**](CommunityControllerApi.md#getAll1)           | **GET** /communities/all/{since} |
| [**getAllUuids2**](CommunityControllerApi.md#getAllUuids2) | **GET** /communities/uuids       |
| [**getByUuids4**](CommunityControllerApi.md#getByUuids4)   | **POST** /communities/query      |

<a name="getAll1"></a>

# **getAll1**

> List&lt;CommunityDto&gt; getAll1(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
Long since = 789L; // Long |
try {
    List<CommunityDto> result = apiInstance.getAll1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAll1");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids2"></a>

# **getAllUuids2**

> List&lt;String&gt; getAllUuids2()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAllUuids2");
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

<a name="getByUuids4"></a>

# **getByUuids4**

> List&lt;CommunityDto&gt; getByUuids4(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<CommunityDto> result = apiInstance.getByUuids4(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getByUuids4");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
