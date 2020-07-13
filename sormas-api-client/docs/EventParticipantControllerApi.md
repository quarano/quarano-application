# EventParticipantControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                            | HTTP request                           | Description |
| ------------------------------------------------------------------------------------------------- | -------------------------------------- | ----------- |
| [**getAllActiveUuids3**](EventParticipantControllerApi.md#getAllActiveUuids3)                     | **GET** /eventparticipants/uuids       |
| [**getAllEventParticipantsAfter**](EventParticipantControllerApi.md#getAllEventParticipantsAfter) | **GET** /eventparticipants/all/{since} |
| [**getByUuids8**](EventParticipantControllerApi.md#getByUuids8)                                   | **POST** /eventparticipants/query      |
| [**postEventParticipants**](EventParticipantControllerApi.md#postEventParticipants)               | **POST** /eventparticipants/push       |

<a name="getAllActiveUuids3"></a>

# **getAllActiveUuids3**

> List&lt;String&gt; getAllActiveUuids3()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids3();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getAllActiveUuids3");
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

<a name="getAllEventParticipantsAfter"></a>

# **getAllEventParticipantsAfter**

> List&lt;EventParticipantDto&gt; getAllEventParticipantsAfter(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
Long since = 789L; // Long |
try {
    List<EventParticipantDto> result = apiInstance.getAllEventParticipantsAfter(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getAllEventParticipantsAfter");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids8"></a>

# **getByUuids8**

> List&lt;EventParticipantDto&gt; getByUuids8(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<EventParticipantDto> result = apiInstance.getByUuids8(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getByUuids8");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postEventParticipants"></a>

# **postEventParticipants**

> List&lt;PushResult&gt; postEventParticipants(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<EventParticipantDto> body = Arrays.asList(new EventParticipantDto()); // List<EventParticipantDto> |
try {
    List<PushResult> result = apiInstance.postEventParticipants(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#postEventParticipants");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                          | Description | Notes      |
| -------- | ------------------------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
