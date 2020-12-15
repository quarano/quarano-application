# FacilityControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllByRegion**](FacilityControllerApi.md#getAllByRegion) | **GET** /facilities/region/{regionUuid}/{since} | 
[**getAllUuids10**](FacilityControllerApi.md#getAllUuids10) | **GET** /facilities/uuids | 
[**getAllWithoutRegion**](FacilityControllerApi.md#getAllWithoutRegion) | **GET** /facilities/general/{since} | 
[**getByUuids15**](FacilityControllerApi.md#getByUuids15) | **POST** /facilities/query | 

<a name="getAllByRegion"></a>
# **getAllByRegion**
> List&lt;FacilityDto&gt; getAllByRegion(regionUuid, since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
String regionUuid = "regionUuid_example"; // String | 
Long since = 789L; // Long | 
try {
    List<FacilityDto> result = apiInstance.getAllByRegion(regionUuid, since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllByRegion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **regionUuid** | **String**|  |
 **since** | **Long**|  |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids10"></a>
# **getAllUuids10**
> List&lt;String&gt; getAllUuids10()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids10();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllUuids10");
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

<a name="getAllWithoutRegion"></a>
# **getAllWithoutRegion**
> List&lt;FacilityDto&gt; getAllWithoutRegion(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
Long since = 789L; // Long | 
try {
    List<FacilityDto> result = apiInstance.getAllWithoutRegion(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllWithoutRegion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids15"></a>
# **getByUuids15**
> List&lt;FacilityDto&gt; getByUuids15(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<FacilityDto> result = apiInstance.getByUuids15(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getByUuids15");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

