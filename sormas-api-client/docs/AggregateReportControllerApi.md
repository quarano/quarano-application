# AggregateReportControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllAggregateReports**](AggregateReportControllerApi.md#getAllAggregateReports) | **GET** /aggregatereports/all/{since} | 
[**getAllUuids1**](AggregateReportControllerApi.md#getAllUuids1) | **GET** /aggregatereports/uuids | 
[**getByUuids2**](AggregateReportControllerApi.md#getByUuids2) | **POST** /aggregatereports/query | 
[**postAggregateReports**](AggregateReportControllerApi.md#postAggregateReports) | **POST** /aggregatereports/push | 

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
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

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

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids1"></a>
# **getAllUuids1**
> List&lt;String&gt; getAllUuids1()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
try {
    List<String> result = apiInstance.getAllUuids1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#getAllUuids1");
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

<a name="getByUuids2"></a>
# **getByUuids2**
> List&lt;AggregateReportDto&gt; getByUuids2(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.AggregateReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

AggregateReportControllerApi apiInstance = new AggregateReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<AggregateReportDto> result = apiInstance.getByUuids2(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregateReportControllerApi#getByUuids2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

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
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

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

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;AggregateReportDto&gt;**](AggregateReportDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

