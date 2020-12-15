# OutbreakControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getActiveSince**](OutbreakControllerApi.md#getActiveSince) | **GET** /outbreaks/active/{since} | 
[**getActiveUuids**](OutbreakControllerApi.md#getActiveUuids) | **GET** /outbreaks/uuids | 
[**getInactiveUuidsSince**](OutbreakControllerApi.md#getInactiveUuidsSince) | **GET** /outbreaks/inactive/{since} | 

<a name="getActiveSince"></a>
# **getActiveSince**
> List&lt;OutbreakDto&gt; getActiveSince(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.OutbreakControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OutbreakControllerApi apiInstance = new OutbreakControllerApi();
Long since = 789L; // Long | 
try {
    List<OutbreakDto> result = apiInstance.getActiveSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OutbreakControllerApi#getActiveSince");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;OutbreakDto&gt;**](OutbreakDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getActiveUuids"></a>
# **getActiveUuids**
> List&lt;String&gt; getActiveUuids()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.OutbreakControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OutbreakControllerApi apiInstance = new OutbreakControllerApi();
try {
    List<String> result = apiInstance.getActiveUuids();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OutbreakControllerApi#getActiveUuids");
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

<a name="getInactiveUuidsSince"></a>
# **getInactiveUuidsSince**
> List&lt;String&gt; getInactiveUuidsSince(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.OutbreakControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OutbreakControllerApi apiInstance = new OutbreakControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getInactiveUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OutbreakControllerApi#getInactiveUuidsSince");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

