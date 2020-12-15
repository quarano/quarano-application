# InfoControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAppUrl**](InfoControllerApi.md#getAppUrl) | **GET** /info/appurl | 
[**getLocale**](InfoControllerApi.md#getLocale) | **GET** /info/locale | 
[**getVersion1**](InfoControllerApi.md#getVersion1) | **GET** /info/version | 
[**isCompatibleToApi**](InfoControllerApi.md#isCompatibleToApi) | **GET** /info/checkcompatibility | 

<a name="getAppUrl"></a>
# **getAppUrl**
> String getAppUrl(appVersion)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.InfoControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

InfoControllerApi apiInstance = new InfoControllerApi();
String appVersion = "appVersion_example"; // String | 
try {
    String result = apiInstance.getAppUrl(appVersion);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoControllerApi#getAppUrl");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **appVersion** | **String**|  | [optional]

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getLocale"></a>
# **getLocale**
> String getLocale()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.InfoControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

InfoControllerApi apiInstance = new InfoControllerApi();
try {
    String result = apiInstance.getLocale();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoControllerApi#getLocale");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getVersion1"></a>
# **getVersion1**
> String getVersion1()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.InfoControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

InfoControllerApi apiInstance = new InfoControllerApi();
try {
    String result = apiInstance.getVersion1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoControllerApi#getVersion1");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="isCompatibleToApi"></a>
# **isCompatibleToApi**
> CompatibilityCheckResponse isCompatibleToApi(appVersion)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.InfoControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

InfoControllerApi apiInstance = new InfoControllerApi();
String appVersion = "appVersion_example"; // String | 
try {
    CompatibilityCheckResponse result = apiInstance.isCompatibleToApi(appVersion);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoControllerApi#isCompatibleToApi");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **appVersion** | **String**|  | [optional]

### Return type

[**CompatibilityCheckResponse**](CompatibilityCheckResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

