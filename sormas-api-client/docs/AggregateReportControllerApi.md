# AggregateReportControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                               | HTTP request                          | Description |
| ------------------------------------------------------------------------------------ | ------------------------------------- | ----------- |
| [**getAllAggregateReports**](AggregateReportControllerApi.md#getAllAggregateReports) | **GET** /aggregatereports/all/{since} |
| [**getAllUuids**](AggregateReportControllerApi.md#getAllUuids)                       | **GET** /aggregatereports/uuids       |
| [**getByUuids1**](AggregateReportControllerApi.md#getByUuids1)                       | **POST** /aggregatereports/query      |
| [**postAggregateReports**](AggregateReportControllerApi.md#postAggregateReports)     | **POST** /aggregatereports/push       |

<a name="getAllAggregateReports"></a>

# **getAllAggregateReports**

> List&lt;AggregateReportDto&gt; getAllAggregateReports(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
Long since = 789L; // Long |
try {
    List<AggregateReportDto> result = apiInstance.getAllAggregateReports(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#getAllAggregateReports");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids"></a>

# **getAllUuids**

> List&lt;String&gt; getAllUuids()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
try {
    List<String> result = apiInstance.getAllUuids();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#getAllUuids");
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

<a name="getByUuids1"></a>

# **getByUuids1**

> List&lt;AggregateReportDto&gt; getByUuids1(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<AggregateReportDto> result = apiInstance.getByUuids1(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#getByUuids1");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postAggregateReports"></a>

# **postAggregateReports**

> List&lt;PushResult&gt; postAggregateReports(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
List<AggregateReportDto> body = Arrays.asList(new AggregateReportDto()); // List<AggregateReportDto> |
try {
    List<PushResult> result = apiInstance.postAggregateReports(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#postAggregateReports");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                        | Description | Notes      |
| -------- | ----------------------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
