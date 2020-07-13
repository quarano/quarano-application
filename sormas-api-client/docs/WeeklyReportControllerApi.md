# WeeklyReportControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                      | HTTP request                       | Description |
| --------------------------------------------------------------------------- | ---------------------------------- | ----------- |
| [**getAllUuids12**](WeeklyReportControllerApi.md#getAllUuids12)             | **GET** /weeklyreports/uuids       |
| [**getAllWeeklyReports**](WeeklyReportControllerApi.md#getAllWeeklyReports) | **GET** /weeklyreports/all/{since} |
| [**getByUuids22**](WeeklyReportControllerApi.md#getByUuids22)               | **POST** /weeklyreports/query      |
| [**postWeeklyReports**](WeeklyReportControllerApi.md#postWeeklyReports)     | **POST** /weeklyreports/push       |

<a name="getAllUuids12"></a>

# **getAllUuids12**

> List&lt;String&gt; getAllUuids12()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
try {
    List<String> result = apiInstance.getAllUuids12();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getAllUuids12");
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

<a name="getAllWeeklyReports"></a>

# **getAllWeeklyReports**

> List&lt;WeeklyReportDto&gt; getAllWeeklyReports(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
Long since = 789L; // Long |
try {
    List<WeeklyReportDto> result = apiInstance.getAllWeeklyReports(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getAllWeeklyReports");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids22"></a>

# **getByUuids22**

> List&lt;WeeklyReportDto&gt; getByUuids22(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<WeeklyReportDto> result = apiInstance.getByUuids22(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getByUuids22");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postWeeklyReports"></a>

# **postWeeklyReports**

> List&lt;PushResult&gt; postWeeklyReports(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
List<WeeklyReportDto> body = Arrays.asList(new WeeklyReportDto()); // List<WeeklyReportDto> |
try {
    List<PushResult> result = apiInstance.postWeeklyReports(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#postWeeklyReports");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                  | Description | Notes      |
| -------- | ----------------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
