# CampaignFormDataControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCampaignFormData**](CampaignFormDataControllerApi.md#getAllCampaignFormData) | **GET** /campaignFormData/all/{since} | 
[**getAllUuids2**](CampaignFormDataControllerApi.md#getAllUuids2) | **GET** /campaignFormData/uuids | 
[**getByUuids3**](CampaignFormDataControllerApi.md#getByUuids3) | **POST** /campaignFormData/query | 
[**postCampaignFormData**](CampaignFormDataControllerApi.md#postCampaignFormData) | **POST** /campaignFormData/push | 

<a name="getAllCampaignFormData"></a>
# **getAllCampaignFormData**
> List&lt;CampaignFormDataDto&gt; getAllCampaignFormData(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormDataControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormDataControllerApi apiInstance = new CampaignFormDataControllerApi();
Long since = 789L; // Long | 
try {
    List<CampaignFormDataDto> result = apiInstance.getAllCampaignFormData(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormDataControllerApi#getAllCampaignFormData");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CampaignFormDataDto&gt;**](CampaignFormDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids2"></a>
# **getAllUuids2**
> List&lt;String&gt; getAllUuids2()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormDataControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormDataControllerApi apiInstance = new CampaignFormDataControllerApi();
try {
    List<String> result = apiInstance.getAllUuids2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormDataControllerApi#getAllUuids2");
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

<a name="getByUuids3"></a>
# **getByUuids3**
> List&lt;CampaignFormDataDto&gt; getByUuids3(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormDataControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormDataControllerApi apiInstance = new CampaignFormDataControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CampaignFormDataDto> result = apiInstance.getByUuids3(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormDataControllerApi#getByUuids3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CampaignFormDataDto&gt;**](CampaignFormDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

<a name="postCampaignFormData"></a>
# **postCampaignFormData**
> List&lt;PushResult&gt; postCampaignFormData(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormDataControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormDataControllerApi apiInstance = new CampaignFormDataControllerApi();
List<CampaignFormDataDto> body = Arrays.asList(new CampaignFormDataDto()); // List<CampaignFormDataDto> | 
try {
    List<PushResult> result = apiInstance.postCampaignFormData(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormDataControllerApi#postCampaignFormData");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;CampaignFormDataDto&gt;**](CampaignFormDataDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

