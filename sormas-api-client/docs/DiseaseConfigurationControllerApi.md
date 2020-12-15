# DiseaseConfigurationControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllDiseaseConfigurations**](DiseaseConfigurationControllerApi.md#getAllDiseaseConfigurations) | **GET** /diseaseconfigurations/all/{since} | 
[**getAllUuids8**](DiseaseConfigurationControllerApi.md#getAllUuids8) | **GET** /diseaseconfigurations/uuids | 
[**getByUuids11**](DiseaseConfigurationControllerApi.md#getByUuids11) | **POST** /diseaseconfigurations/query | 

<a name="getAllDiseaseConfigurations"></a>
# **getAllDiseaseConfigurations**
> List&lt;DiseaseConfigurationDto&gt; getAllDiseaseConfigurations(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<DiseaseConfigurationDto> result = apiInstance.getAllDiseaseConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllDiseaseConfigurations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids8"></a>
# **getAllUuids8**
> List&lt;String&gt; getAllUuids8()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids8();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllUuids8");
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

<a name="getByUuids11"></a>
# **getByUuids11**
> List&lt;DiseaseConfigurationDto&gt; getByUuids11(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<DiseaseConfigurationDto> result = apiInstance.getByUuids11(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getByUuids11");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

