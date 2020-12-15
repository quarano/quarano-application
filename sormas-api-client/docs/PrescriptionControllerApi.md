# PrescriptionControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids6**](PrescriptionControllerApi.md#getAllActiveUuids6) | **GET** /prescriptions/uuids | 
[**getAllPrescriptions**](PrescriptionControllerApi.md#getAllPrescriptions) | **GET** /prescriptions/all/{since} | 
[**getByUuids20**](PrescriptionControllerApi.md#getByUuids20) | **POST** /prescriptions/query | 
[**postPrescriptions**](PrescriptionControllerApi.md#postPrescriptions) | **POST** /prescriptions/push | 

<a name="getAllActiveUuids6"></a>
# **getAllActiveUuids6**
> List&lt;String&gt; getAllActiveUuids6()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PrescriptionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PrescriptionControllerApi apiInstance = new PrescriptionControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids6();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PrescriptionControllerApi#getAllActiveUuids6");
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

<a name="getAllPrescriptions"></a>
# **getAllPrescriptions**
> List&lt;PrescriptionDto&gt; getAllPrescriptions(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PrescriptionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PrescriptionControllerApi apiInstance = new PrescriptionControllerApi();
Long since = 789L; // Long | 
try {
    List<PrescriptionDto> result = apiInstance.getAllPrescriptions(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PrescriptionControllerApi#getAllPrescriptions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PrescriptionDto&gt;**](PrescriptionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids20"></a>
# **getByUuids20**
> List&lt;PrescriptionDto&gt; getByUuids20(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PrescriptionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PrescriptionControllerApi apiInstance = new PrescriptionControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PrescriptionDto> result = apiInstance.getByUuids20(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PrescriptionControllerApi#getByUuids20");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PrescriptionDto&gt;**](PrescriptionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postPrescriptions"></a>
# **postPrescriptions**
> List&lt;PushResult&gt; postPrescriptions(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PrescriptionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PrescriptionControllerApi apiInstance = new PrescriptionControllerApi();
List<PrescriptionDto> body = Arrays.asList(new PrescriptionDto()); // List<PrescriptionDto> | 
try {
    List<PushResult> result = apiInstance.postPrescriptions(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PrescriptionControllerApi#postPrescriptions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;PrescriptionDto&gt;**](PrescriptionDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

