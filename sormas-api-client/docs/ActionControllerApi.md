# ActionControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll**](ActionControllerApi.md#getAll) | **GET** /actions/all/{since} | 
[**getAllUuids**](ActionControllerApi.md#getAllUuids) | **GET** /actions/uuids | 
[**getByUuids**](ActionControllerApi.md#getByUuids) | **POST** /actions/query | 
[**postActions**](ActionControllerApi.md#postActions) | **POST** /actions/push | 

<a name="getAll"></a>
# **getAll**
> List&lt;ActionDto&gt; getAll(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ActionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ActionControllerApi apiInstance = new ActionControllerApi();
Long since = 789L; // Long | 
try {
    List<ActionDto> result = apiInstance.getAll(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActionControllerApi#getAll");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;ActionDto&gt;**](ActionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

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
//import de.quarano.sormas.client.api.ActionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ActionControllerApi apiInstance = new ActionControllerApi();
try {
    List<String> result = apiInstance.getAllUuids();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActionControllerApi#getAllUuids");
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

<a name="getByUuids"></a>
# **getByUuids**
> List&lt;ActionDto&gt; getByUuids(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ActionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ActionControllerApi apiInstance = new ActionControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<ActionDto> result = apiInstance.getByUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActionControllerApi#getByUuids");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;ActionDto&gt;**](ActionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postActions"></a>
# **postActions**
> List&lt;PushResult&gt; postActions(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ActionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ActionControllerApi apiInstance = new ActionControllerApi();
List<ActionDto> body = Arrays.asList(new ActionDto()); // List<ActionDto> | 
try {
    List<PushResult> result = apiInstance.postActions(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActionControllerApi#postActions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ActionDto&gt;**](ActionDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

