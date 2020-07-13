# CaseControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                                        | HTTP request                                            | Description |
| ------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------- | ----------- |
| [**getAllCases**](CaseControllerApi.md#getAllCases)                                                           | **GET** /cases/all/{since}                              |
| [**getAllCasesWithExtendedChangeDateFilters**](CaseControllerApi.md#getAllCasesWithExtendedChangeDateFilters) | **GET** /cases/allWithExtendedChangeDateFilters/{since} |
| [**getAllUuids1**](CaseControllerApi.md#getAllUuids1)                                                         | **GET** /cases/uuids                                    |
| [**getArchivedUuidsSince**](CaseControllerApi.md#getArchivedUuidsSince)                                       | **GET** /cases/archived/{since}                         |
| [**getByUuids2**](CaseControllerApi.md#getByUuids2)                                                           | **POST** /cases/query                                   |
| [**getDeletedUuidsSince**](CaseControllerApi.md#getDeletedUuidsSince)                                         | **GET** /cases/deleted/{since}                          |
| [**postCases**](CaseControllerApi.md#postCases)                                                               | **POST** /cases/push                                    |

<a name="getAllCases"></a>

# **getAllCases**

> List&lt;CaseDataDto&gt; getAllCases(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long |
try {
    List<CaseDataDto> result = apiInstance.getAllCases(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllCases");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllCasesWithExtendedChangeDateFilters"></a>

# **getAllCasesWithExtendedChangeDateFilters**

> List&lt;CaseDataDto&gt; getAllCasesWithExtendedChangeDateFilters(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long |
try {
    List<CaseDataDto> result = apiInstance.getAllCasesWithExtendedChangeDateFilters(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllCasesWithExtendedChangeDateFilters");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids1"></a>

# **getAllUuids1**

> List&lt;String&gt; getAllUuids1()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
try {
    List<String> result = apiInstance.getAllUuids1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllUuids1");
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

<a name="getArchivedUuidsSince"></a>

# **getArchivedUuidsSince**

> List&lt;String&gt; getArchivedUuidsSince(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getArchivedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getArchivedUuidsSince");
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

<a name="getByUuids2"></a>

# **getByUuids2**

> List&lt;CaseDataDto&gt; getByUuids2(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<CaseDataDto> result = apiInstance.getByUuids2(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getByUuids2");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince"></a>

# **getDeletedUuidsSince**

> List&lt;String&gt; getDeletedUuidsSince(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getDeletedUuidsSince");
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

<a name="postCases"></a>

# **postCases**

> List&lt;PushResult&gt; postCases(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

CaseControllerApi apiInstance = new CaseControllerApi();
List<CaseDataDto> body = Arrays.asList(new CaseDataDto()); // List<CaseDataDto> |
try {
    List<PushResult> result = apiInstance.postCases(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#postCases");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                          | Description | Notes      |
| -------- | --------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;CaseDataDto&gt;**](CaseDataDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
