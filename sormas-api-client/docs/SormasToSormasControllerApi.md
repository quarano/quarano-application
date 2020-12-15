# SormasToSormasControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**saveReturnedCase**](SormasToSormasControllerApi.md#saveReturnedCase) | **PUT** /sormasToSormas/cases | 
[**saveReturnedContact**](SormasToSormasControllerApi.md#saveReturnedContact) | **PUT** /sormasToSormas/contacts | 
[**saveSharedCase**](SormasToSormasControllerApi.md#saveSharedCase) | **POST** /sormasToSormas/cases | 
[**saveSharedContact**](SormasToSormasControllerApi.md#saveSharedContact) | **POST** /sormasToSormas/contacts | 

<a name="saveReturnedCase"></a>
# **saveReturnedCase**
> saveReturnedCase(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveReturnedCase(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveReturnedCase");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveReturnedContact"></a>
# **saveReturnedContact**
> saveReturnedContact(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveReturnedContact(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveReturnedContact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedCase"></a>
# **saveSharedCase**
> saveSharedCase(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedCase(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedCase");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedContact"></a>
# **saveSharedContact**
> saveSharedContact(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedContact(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedContact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

