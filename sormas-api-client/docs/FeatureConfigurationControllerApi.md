# FeatureConfigurationControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllFeatureConfigurations**](FeatureConfigurationControllerApi.md#getAllFeatureConfigurations) | **GET** /featureconfigurations/all/{since} | 
[**getAllUuids11**](FeatureConfigurationControllerApi.md#getAllUuids11) | **GET** /featureconfigurations/uuids | 
[**getByUuids16**](FeatureConfigurationControllerApi.md#getByUuids16) | **POST** /featureconfigurations/query | 
[**getDeletedUuids**](FeatureConfigurationControllerApi.md#getDeletedUuids) | **GET** /featureconfigurations/deleted/{since} | 

<a name="getAllFeatureConfigurations"></a>
# **getAllFeatureConfigurations**
> List&lt;FeatureConfigurationDto&gt; getAllFeatureConfigurations(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<FeatureConfigurationDto> result = apiInstance.getAllFeatureConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllFeatureConfigurations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids11"></a>
# **getAllUuids11**
> List&lt;String&gt; getAllUuids11()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids11();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllUuids11");
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

<a name="getByUuids16"></a>
# **getByUuids16**
> List&lt;FeatureConfigurationDto&gt; getByUuids16(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<FeatureConfigurationDto> result = apiInstance.getByUuids16(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getByUuids16");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuids"></a>
# **getDeletedUuids**
> List&lt;String&gt; getDeletedUuids(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuids(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getDeletedUuids");
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

