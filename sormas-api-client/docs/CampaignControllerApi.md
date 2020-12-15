# CampaignControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCampaignFormData1**](CampaignControllerApi.md#getAllCampaignFormData1) | **GET** /campaigns/all/{since} | 
[**getAllUuids4**](CampaignControllerApi.md#getAllUuids4) | **GET** /campaigns/uuids | 
[**getByUuids5**](CampaignControllerApi.md#getByUuids5) | **POST** /campaigns/query | 

<a name="getAllCampaignFormData1"></a>
# **getAllCampaignFormData1**
> List&lt;CampaignDto&gt; getAllCampaignFormData1(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignControllerApi apiInstance = new CampaignControllerApi();
Long since = 789L; // Long | 
try {
    List<CampaignDto> result = apiInstance.getAllCampaignFormData1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getAllCampaignFormData1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CampaignDto&gt;**](CampaignDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids4"></a>
# **getAllUuids4**
> List&lt;String&gt; getAllUuids4()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignControllerApi apiInstance = new CampaignControllerApi();
try {
    List<String> result = apiInstance.getAllUuids4();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getAllUuids4");
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

<a name="getByUuids5"></a>
# **getByUuids5**
> List&lt;CampaignDto&gt; getByUuids5(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignControllerApi apiInstance = new CampaignControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CampaignDto> result = apiInstance.getByUuids5(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getByUuids5");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CampaignDto&gt;**](CampaignDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

