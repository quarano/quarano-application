# CampaignFormMetaControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCampaignFormMeta**](CampaignFormMetaControllerApi.md#getAllCampaignFormMeta) | **GET** /campaignFormMeta/all/{since} | 
[**getAllUuids3**](CampaignFormMetaControllerApi.md#getAllUuids3) | **GET** /campaignFormMeta/uuids | 
[**getByUuids4**](CampaignFormMetaControllerApi.md#getByUuids4) | **POST** /campaignFormMeta/query | 

<a name="getAllCampaignFormMeta"></a>
# **getAllCampaignFormMeta**
> List&lt;CampaignFormMetaDto&gt; getAllCampaignFormMeta(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
Long since = 789L; // Long | 
try {
    List<CampaignFormMetaDto> result = apiInstance.getAllCampaignFormMeta(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getAllCampaignFormMeta");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CampaignFormMetaDto&gt;**](CampaignFormMetaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids3"></a>
# **getAllUuids3**
> List&lt;String&gt; getAllUuids3()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
try {
    List<String> result = apiInstance.getAllUuids3();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getAllUuids3");
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

<a name="getByUuids4"></a>
# **getByUuids4**
> List&lt;CampaignFormMetaDto&gt; getByUuids4(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CampaignFormMetaDto> result = apiInstance.getByUuids4(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getByUuids4");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CampaignFormMetaDto&gt;**](CampaignFormMetaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

