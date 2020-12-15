# DistrictControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll4**](DistrictControllerApi.md#getAll4) | **GET** /districts/all/{since} | 
[**getAllUuids9**](DistrictControllerApi.md#getAllUuids9) | **GET** /districts/uuids | 
[**getByUuids12**](DistrictControllerApi.md#getByUuids12) | **POST** /districts/query | 

<a name="getAll4"></a>
# **getAll4**
> List&lt;DistrictDto&gt; getAll4(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
Long since = 789L; // Long | 
try {
    List<DistrictDto> result = apiInstance.getAll4(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAll4");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

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
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
try {
    List<String> result = apiInstance.getAllUuids9();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAllUuids9");
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

<a name="getByUuids12"></a>
# **getByUuids12**
> List&lt;DistrictDto&gt; getByUuids12(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DistrictControllerApi apiInstance = new DistrictControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<DistrictDto> result = apiInstance.getByUuids12(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getByUuids12");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

