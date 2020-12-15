# PathogenTestControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids5**](PathogenTestControllerApi.md#getAllActiveUuids5) | **GET** /pathogentests/uuids | 
[**getAllPathogenTests**](PathogenTestControllerApi.md#getAllPathogenTests) | **GET** /pathogentests/all/{since} | 
[**getBySampleUuids**](PathogenTestControllerApi.md#getBySampleUuids) | **POST** /pathogentests/query/samples | 
[**getByUuids17**](PathogenTestControllerApi.md#getByUuids17) | **POST** /pathogentests/query | 
[**getDeletedUuidsSince4**](PathogenTestControllerApi.md#getDeletedUuidsSince4) | **GET** /pathogentests/deleted/{since} | 
[**postPathogenTests**](PathogenTestControllerApi.md#postPathogenTests) | **POST** /pathogentests/push | 

<a name="getAllActiveUuids5"></a>
# **getAllActiveUuids5**
> List&lt;String&gt; getAllActiveUuids5()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids5();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getAllActiveUuids5");
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

<a name="getAllPathogenTests"></a>
# **getAllPathogenTests**
> List&lt;PathogenTestDto&gt; getAllPathogenTests(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
Long since = 789L; // Long | 
try {
    List<PathogenTestDto> result = apiInstance.getAllPathogenTests(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getAllPathogenTests");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getBySampleUuids"></a>
# **getBySampleUuids**
> List&lt;PathogenTestDto&gt; getBySampleUuids(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PathogenTestDto> result = apiInstance.getBySampleUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getBySampleUuids");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids17"></a>
# **getByUuids17**
> List&lt;PathogenTestDto&gt; getByUuids17(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PathogenTestDto> result = apiInstance.getByUuids17(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getByUuids17");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince4"></a>
# **getDeletedUuidsSince4**
> List&lt;String&gt; getDeletedUuidsSince4(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince4(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getDeletedUuidsSince4");
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

<a name="postPathogenTests"></a>
# **postPathogenTests**
> List&lt;PushResult&gt; postPathogenTests(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<PathogenTestDto> body = Arrays.asList(new PathogenTestDto()); // List<PathogenTestDto> | 
try {
    List<PushResult> result = apiInstance.postPathogenTests(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#postPathogenTests");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

