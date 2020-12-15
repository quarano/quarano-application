# CommunityControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll2**](CommunityControllerApi.md#getAll2) | **GET** /communities/all/{since} | 
[**getAllUuids6**](CommunityControllerApi.md#getAllUuids6) | **GET** /communities/uuids | 
[**getByUuids8**](CommunityControllerApi.md#getByUuids8) | **POST** /communities/query | 

<a name="getAll2"></a>
# **getAll2**
> List&lt;CommunityDto&gt; getAll2(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
Long since = 789L; // Long | 
try {
    List<CommunityDto> result = apiInstance.getAll2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAll2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids6"></a>
# **getAllUuids6**
> List&lt;String&gt; getAllUuids6()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids6();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAllUuids6");
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

<a name="getByUuids8"></a>
# **getByUuids8**
> List&lt;CommunityDto&gt; getByUuids8(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CommunityControllerApi apiInstance = new CommunityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CommunityDto> result = apiInstance.getByUuids8(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getByUuids8");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

