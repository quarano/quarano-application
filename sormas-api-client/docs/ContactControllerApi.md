# ContactControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                     | HTTP request                      | Description |
| -------------------------------------------------------------------------- | --------------------------------- | ----------- |
| [**getAllActiveUuids2**](ContactControllerApi.md#getAllActiveUuids2)       | **GET** /contacts/uuids           |
| [**getAllContacts**](ContactControllerApi.md#getAllContacts)               | **GET** /contacts/all/{since}     |
| [**getByUuids5**](ContactControllerApi.md#getByUuids5)                     | **POST** /contacts/query          |
| [**getDeletedUuidsSince1**](ContactControllerApi.md#getDeletedUuidsSince1) | **GET** /contacts/deleted/{since} |
| [**postContacts**](ContactControllerApi.md#postContacts)                   | **POST** /contacts/push           |

<a name="getAllActiveUuids2"></a>

# **getAllActiveUuids2**

> List&lt;String&gt; getAllActiveUuids2()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ContactControllerApi apiInstance = new ContactControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getAllActiveUuids2");
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

<a name="getAllContacts"></a>

# **getAllContacts**

> List&lt;ContactDto&gt; getAllContacts(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ContactControllerApi apiInstance = new ContactControllerApi();
Long since = 789L; // Long |
try {
    List<ContactDto> result = apiInstance.getAllContacts(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getAllContacts");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;ContactDto&gt;**](ContactDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids5"></a>

# **getByUuids5**

> List&lt;ContactDto&gt; getByUuids5(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ContactControllerApi apiInstance = new ContactControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<ContactDto> result = apiInstance.getByUuids5(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getByUuids5");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;ContactDto&gt;**](ContactDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince1"></a>

# **getDeletedUuidsSince1**

> List&lt;String&gt; getDeletedUuidsSince1(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ContactControllerApi apiInstance = new ContactControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuidsSince1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getDeletedUuidsSince1");
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

<a name="postContacts"></a>

# **postContacts**

> List&lt;PushResult&gt; postContacts(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ContactControllerApi apiInstance = new ContactControllerApi();
List<ContactDto> body = Arrays.asList(new ContactDto()); // List<ContactDto> |
try {
    List<PushResult> result = apiInstance.postContacts(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#postContacts");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                        | Description | Notes      |
| -------- | ------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;ContactDto&gt;**](ContactDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
