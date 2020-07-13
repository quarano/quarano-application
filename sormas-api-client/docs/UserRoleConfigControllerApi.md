# UserRoleConfigControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                  | HTTP request                       | Description |
| ----------------------------------------------------------------------- | ---------------------------------- | ----------- |
| [**getAll7**](UserRoleConfigControllerApi.md#getAll7)                   | **GET** /userroles/all/{since}     |
| [**getAllUuids11**](UserRoleConfigControllerApi.md#getAllUuids11)       | **GET** /userroles/uuids           |
| [**getDeletedUuids1**](UserRoleConfigControllerApi.md#getDeletedUuids1) | **GET** /userroles/deleted/{since} |

<a name="getAll7"></a>

# **getAll7**

> List&lt;UserRoleConfigDto&gt; getAll7(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
Long since = 789L; // Long |
try {
    List<UserRoleConfigDto> result = apiInstance.getAll7(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAll7");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;UserRoleConfigDto&gt;**](UserRoleConfigDto.md)

### Authorization

[http-basic](../README.md#http-basic)

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
//import de.quarano.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
try {
    List<String> result = apiInstance.getAllUuids11();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAllUuids11");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[http-basic](../README.md#http-basic)

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
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

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

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

**List&lt;String&gt;**

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8
