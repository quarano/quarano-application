# EventControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                     | HTTP request                     | Description |
| -------------------------------------------------------------------------- | -------------------------------- | ----------- |
| [**getAllActiveUuids4**](EventControllerApi.md#getAllActiveUuids4)         | **GET** /events/uuids            |
| [**getAllEvents**](EventControllerApi.md#getAllEvents)                     | **GET** /events/all/{since}      |
| [**getArchivedUuidsSince1**](EventControllerApi.md#getArchivedUuidsSince1) | **GET** /events/archived/{since} |
| [**getByUuids9**](EventControllerApi.md#getByUuids9)                       | **POST** /events/query           |
| [**getDeletedUuidsSince2**](EventControllerApi.md#getDeletedUuidsSince2)   | **GET** /events/deleted/{since}  |
| [**postEvents**](EventControllerApi.md#postEvents)                         | **POST** /events/push            |

<a name="getAllActiveUuids4"></a>

# **getAllActiveUuids4**

> List&lt;String&gt; getAllActiveUuids4()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids4();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getAllActiveUuids4");
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

<a name="getAllEvents"></a>

# **getAllEvents**

> List&lt;EventDto&gt; getAllEvents(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long |
try {
    List<EventDto> result = apiInstance.getAllEvents(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getAllEvents");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;EventDto&gt;**](EventDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getArchivedUuidsSince1"></a>

# **getArchivedUuidsSince1**

> List&lt;String&gt; getArchivedUuidsSince1(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getArchivedUuidsSince1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getArchivedUuidsSince1");
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

<a name="getByUuids9"></a>

# **getByUuids9**

> List&lt;EventDto&gt; getByUuids9(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<EventDto> result = apiInstance.getByUuids9(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getByUuids9");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;EventDto&gt;**](EventDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince2"></a>

# **getDeletedUuidsSince2**

> List&lt;String&gt; getDeletedUuidsSince2(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuidsSince2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getDeletedUuidsSince2");
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

<a name="postEvents"></a>

# **postEvents**

> List&lt;PushResult&gt; postEvents(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventControllerApi apiInstance = new EventControllerApi();
List<EventDto> body = Arrays.asList(new EventDto()); // List<EventDto> |
try {
    List<PushResult> result = apiInstance.postEvents(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#postEvents");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                    | Description | Notes      |
| -------- | --------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;EventDto&gt;**](EventDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
