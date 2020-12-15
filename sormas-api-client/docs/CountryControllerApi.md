# CountryControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll3**](CountryControllerApi.md#getAll3) | **GET** /countries/all/{since} | 
[**getAllUuids7**](CountryControllerApi.md#getAllUuids7) | **GET** /countries/uuids | 
[**getByUuids10**](CountryControllerApi.md#getByUuids10) | **POST** /countries/query | 

<a name="getAll3"></a>
# **getAll3**
> List&lt;CountryDto&gt; getAll3(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CountryControllerApi apiInstance = new CountryControllerApi();
Long since = 789L; // Long | 
try {
    List<CountryDto> result = apiInstance.getAll3(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getAll3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CountryDto&gt;**](CountryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids7"></a>
# **getAllUuids7**
> List&lt;String&gt; getAllUuids7()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CountryControllerApi apiInstance = new CountryControllerApi();
try {
    List<String> result = apiInstance.getAllUuids7();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getAllUuids7");
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

<a name="getByUuids10"></a>
# **getByUuids10**
> List&lt;CountryDto&gt; getByUuids10(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

CountryControllerApi apiInstance = new CountryControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CountryDto> result = apiInstance.getByUuids10(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getByUuids10");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CountryDto&gt;**](CountryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

