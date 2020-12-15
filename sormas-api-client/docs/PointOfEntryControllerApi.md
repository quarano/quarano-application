# PointOfEntryControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll5**](PointOfEntryControllerApi.md#getAll5) | **GET** /pointsofentry/all/{since} | 
[**getAllUuids13**](PointOfEntryControllerApi.md#getAllUuids13) | **GET** /pointsofentry/uuids | 
[**getByUuids19**](PointOfEntryControllerApi.md#getByUuids19) | **POST** /pointsofentry/query | 

<a name="getAll5"></a>
# **getAll5**
> List&lt;PointOfEntryDto&gt; getAll5(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
Long since = 789L; // Long | 
try {
    List<PointOfEntryDto> result = apiInstance.getAll5(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAll5");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids13"></a>
# **getAllUuids13**
> List&lt;String&gt; getAllUuids13()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
try {
    List<String> result = apiInstance.getAllUuids13();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAllUuids13");
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

<a name="getByUuids19"></a>
# **getByUuids19**
> List&lt;PointOfEntryDto&gt; getByUuids19(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PointOfEntryDto> result = apiInstance.getByUuids19(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getByUuids19");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

