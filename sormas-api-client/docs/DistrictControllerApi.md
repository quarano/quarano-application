# DistrictControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                    | HTTP request                   | Description |
| --------------------------------------------------------- | ------------------------------ | ----------- |
| [**getAll2**](DistrictControllerApi.md#getAll2)           | **GET** /districts/all/{since} |
| [**getAllUuids4**](DistrictControllerApi.md#getAllUuids4) | **GET** /districts/uuids       |
| [**getByUuids7**](DistrictControllerApi.md#getByUuids7)   | **POST** /districts/query      |

<a name="getAll2"></a>

# **getAll2**

> List&lt;DistrictDto&gt; getAll2(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
Long since = 789L; // Long |
try {
    List<DistrictDto> result = apiInstance.getAll2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAll2");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids4"></a>

# **getAllUuids4**

> List&lt;String&gt; getAllUuids4()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
try {
    List<String> result = apiInstance.getAllUuids4();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAllUuids4");
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

<a name="getByUuids7"></a>

# **getByUuids7**

> List&lt;DistrictDto&gt; getByUuids7(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<DistrictDto> result = apiInstance.getByUuids7(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getByUuids7");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
