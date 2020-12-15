# UserRoleConfigControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll9**](UserRoleConfigControllerApi.md#getAll9) | **GET** /userroles/all/{since} | 
[**getAllUuids16**](UserRoleConfigControllerApi.md#getAllUuids16) | **GET** /userroles/uuids | 
[**getDeletedUuids1**](UserRoleConfigControllerApi.md#getDeletedUuids1) | **GET** /userroles/deleted/{since} | 

<a name="getAll9"></a>
# **getAll9**
> List&lt;UserRoleConfigDto&gt; getAll9(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
Long since = 789L; // Long | 
try {
    List<UserRoleConfigDto> result = apiInstance.getAll9(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAll9");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;UserRoleConfigDto&gt;**](UserRoleConfigDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids16"></a>
# **getAllUuids16**
> List&lt;String&gt; getAllUuids16()



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
try {
    List<String> result = apiInstance.getAllUuids16();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAllUuids16");
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

<a name="getDeletedUuids1"></a>
# **getDeletedUuids1**
> List&lt;String&gt; getDeletedUuids1(since)



### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuids1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getDeletedUuids1");
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

