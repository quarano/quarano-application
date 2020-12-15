# CaseControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCases**](CaseControllerApi.md#getAllCases) | **GET** /cases/all/{since} | 
[**getAllUuids5**](CaseControllerApi.md#getAllUuids5) | **GET** /cases/uuids | 
[**getArchivedUuidsSince**](CaseControllerApi.md#getArchivedUuidsSince) | **GET** /cases/archived/{since} | 
[**getByUuids6**](CaseControllerApi.md#getByUuids6) | **POST** /cases/query | 
[**getDeletedUuidsSince**](CaseControllerApi.md#getDeletedUuidsSince) | **GET** /cases/deleted/{since} | 
[**postCases**](CaseControllerApi.md#postCases) | **POST** /cases/push | 

<a name="getAllCases"></a>
# **getAllCases**
> List&lt;CaseDataDto&gt; getAllCases(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<CaseDataDto> result = apiInstance.getAllCases(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids5"></a>
# **getAllUuids5**
> List&lt;String&gt; getAllUuids5()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
try {
    List<String> result = apiInstance.getAllUuids5();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllUuids5");
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

<a name="getArchivedUuidsSince"></a>
# **getArchivedUuidsSince**
> List&lt;String&gt; getArchivedUuidsSince(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getArchivedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getArchivedUuidsSince");
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

<a name="getByUuids6"></a>
# **getByUuids6**
> List&lt;CaseDataDto&gt; getByUuids6(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CaseDataDto> result = apiInstance.getByUuids6(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getByUuids6");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince"></a>
# **getDeletedUuidsSince**
> List&lt;String&gt; getDeletedUuidsSince(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getDeletedUuidsSince");
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

<a name="postCases"></a>
# **postCases**
> List&lt;PushResult&gt; postCases(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
List<CaseDataDto> body = Arrays.asList(new CaseDataDto()); // List<CaseDataDto> | 
try {
    List<PushResult> result = apiInstance.postCases(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#postCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;CaseDataDto&gt;**](CaseDataDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

