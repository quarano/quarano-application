# PersonControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllPersons**](PersonControllerApi.md#getAllPersons) | **GET** /persons/all/{since} | 
[**getAllUuids12**](PersonControllerApi.md#getAllUuids12) | **GET** /persons/uuids | 
[**getByUuids18**](PersonControllerApi.md#getByUuids18) | **POST** /persons/query | 
[**postPersons**](PersonControllerApi.md#postPersons) | **POST** /persons/push | 

<a name="getAllPersons"></a>
# **getAllPersons**
> List&lt;PersonDto&gt; getAllPersons(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
Long since = 789L; // Long | 
try {
    List<PersonDto> result = apiInstance.getAllPersons(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllPersons");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids12"></a>
# **getAllUuids12**
> List&lt;String&gt; getAllUuids12()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
try {
    List<String> result = apiInstance.getAllUuids12();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllUuids12");
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

<a name="getByUuids18"></a>
# **getByUuids18**
> List&lt;PersonDto&gt; getByUuids18(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PersonDto> result = apiInstance.getByUuids18(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getByUuids18");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postPersons"></a>
# **postPersons**
> List&lt;PushResult&gt; postPersons(body)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
List<PersonDto> body = Arrays.asList(new PersonDto()); // List<PersonDto> | 
try {
    List<PushResult> result = apiInstance.postPersons(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#postPersons");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;PersonDto&gt;**](PersonDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

