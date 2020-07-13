# SampleControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                    | HTTP request                     | Description |
| ------------------------------------------------------------------------- | -------------------------------- | ----------- |
| [**getAllActiveUuids7**](SampleControllerApi.md#getAllActiveUuids7)       | **GET** /samples/uuids           |
| [**getAllSamples**](SampleControllerApi.md#getAllSamples)                 | **GET** /samples/all/{since}     |
| [**getByCaseUuids**](SampleControllerApi.md#getByCaseUuids)               | **POST** /samples/query/cases    |
| [**getByUuids17**](SampleControllerApi.md#getByUuids17)                   | **POST** /samples/query          |
| [**getDeletedUuidsSince4**](SampleControllerApi.md#getDeletedUuidsSince4) | **GET** /samples/deleted/{since} |
| [**postSamples**](SampleControllerApi.md#postSamples)                     | **POST** /samples/push           |

<a name="getAllActiveUuids7"></a>

# **getAllActiveUuids7**

> List&lt;String&gt; getAllActiveUuids7()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids7();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#getAllActiveUuids7");
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

<a name="getAllSamples"></a>

# **getAllSamples**

> List&lt;SampleDto&gt; getAllSamples(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
Long since = 789L; // Long |
try {
    List<SampleDto> result = apiInstance.getAllSamples(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#getAllSamples");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;SampleDto&gt;**](SampleDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByCaseUuids"></a>

# **getByCaseUuids**

> List&lt;SampleDto&gt; getByCaseUuids(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<SampleDto> result = apiInstance.getByCaseUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#getByCaseUuids");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;SampleDto&gt;**](SampleDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids17"></a>

# **getByUuids17**

> List&lt;SampleDto&gt; getByUuids17(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<SampleDto> result = apiInstance.getByUuids17(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#getByUuids17");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;SampleDto&gt;**](SampleDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince4"></a>

# **getDeletedUuidsSince4**

> List&lt;String&gt; getDeletedUuidsSince4(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuidsSince4(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#getDeletedUuidsSince4");
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

<a name="postSamples"></a>

# **postSamples**

> List&lt;PushResult&gt; postSamples(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.SampleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

SampleControllerApi apiInstance = new SampleControllerApi();
List<SampleDto> body = Arrays.asList(new SampleDto()); // List<SampleDto> |
try {
    List<PushResult> result = apiInstance.postSamples(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SampleControllerApi#postSamples");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                      | Description | Notes      |
| -------- | ----------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;SampleDto&gt;**](SampleDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
