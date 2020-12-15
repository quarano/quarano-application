# RegionControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll6**](RegionControllerApi.md#getAll6) | **GET** /regions/all/{since} | 
[**getAllUuids14**](RegionControllerApi.md#getAllUuids14) | **GET** /regions/uuids | 
[**getByUuids21**](RegionControllerApi.md#getByUuids21) | **POST** /regions/query | 

<a name="getAll6"></a>
# **getAll6**
> List&lt;RegionDto&gt; getAll6(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
Long since = 789L; // Long | 
try {
    List<RegionDto> result = apiInstance.getAll6(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAll6");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids14"></a>
# **getAllUuids14**
> List&lt;String&gt; getAllUuids14()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
try {
    List<String> result = apiInstance.getAllUuids14();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAllUuids14");
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

<a name="getByUuids21"></a>
# **getByUuids21**
> List&lt;RegionDto&gt; getByUuids21(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

RegionControllerApi apiInstance = new RegionControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<RegionDto> result = apiInstance.getByUuids21(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getByUuids21");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

