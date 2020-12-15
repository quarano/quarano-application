# WeeklyReportControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllUuids17**](WeeklyReportControllerApi.md#getAllUuids17) | **GET** /weeklyreports/uuids | 
[**getAllWeeklyReports**](WeeklyReportControllerApi.md#getAllWeeklyReports) | **GET** /weeklyreports/all/{since} | 
[**getByUuids27**](WeeklyReportControllerApi.md#getByUuids27) | **POST** /weeklyreports/query | 
[**postWeeklyReports**](WeeklyReportControllerApi.md#postWeeklyReports) | **POST** /weeklyreports/push | 

<a name="getAllUuids17"></a>
# **getAllUuids17**
> List&lt;String&gt; getAllUuids17()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
try {
    List<String> result = apiInstance.getAllUuids17();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getAllUuids17");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)

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
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

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

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids27"></a>
# **getByUuids27**
> List&lt;WeeklyReportDto&gt; getByUuids27(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<WeeklyReportDto> result = apiInstance.getByUuids27(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getByUuids27");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

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
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

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

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

