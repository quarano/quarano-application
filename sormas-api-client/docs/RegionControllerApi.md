# RegionControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                  | HTTP request                 | Description |
| ------------------------------------------------------- | ---------------------------- | ----------- |
| [**getAll4**](RegionControllerApi.md#getAll4)           | **GET** /regions/all/{since} |
| [**getAllUuids9**](RegionControllerApi.md#getAllUuids9) | **GET** /regions/uuids       |
| [**getByUuids16**](RegionControllerApi.md#getByUuids16) | **POST** /regions/query      |

<a name="getAll4"></a>

# **getAll4**

> List&lt;RegionDto&gt; getAll4(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
Long since = 789L; // Long |
try {
    List<RegionDto> result = apiInstance.getAll4(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAll4");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids9"></a>

# **getAllUuids9**

> List&lt;String&gt; getAllUuids9()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
try {
    List<String> result = apiInstance.getAllUuids9();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAllUuids9");
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

<a name="getByUuids16"></a>

# **getByUuids16**

> List&lt;RegionDto&gt; getByUuids16(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<RegionDto> result = apiInstance.getByUuids16(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getByUuids16");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
