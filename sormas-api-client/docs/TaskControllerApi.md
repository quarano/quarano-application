# TaskControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                            | HTTP request               | Description |
| ----------------------------------------------------------------- | -------------------------- | ----------- |
| [**getAll5**](TaskControllerApi.md#getAll5)                       | **GET** /tasks/all/{since} |
| [**getAllActiveUuids8**](TaskControllerApi.md#getAllActiveUuids8) | **GET** /tasks/uuids       |
| [**getByUuids18**](TaskControllerApi.md#getByUuids18)             | **POST** /tasks/query      |
| [**postTasks**](TaskControllerApi.md#postTasks)                   | **POST** /tasks/push       |

<a name="getAll5"></a>

# **getAll5**

> List&lt;TaskDto&gt; getAll5(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TaskControllerApi apiInstance = new TaskControllerApi();
Long since = 789L; // Long |
try {
    List<TaskDto> result = apiInstance.getAll5(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getAll5");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;TaskDto&gt;**](TaskDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllActiveUuids8"></a>

# **getAllActiveUuids8**

> List&lt;String&gt; getAllActiveUuids8()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TaskControllerApi apiInstance = new TaskControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids8();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getAllActiveUuids8");
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

<a name="getByUuids18"></a>

# **getByUuids18**

> List&lt;TaskDto&gt; getByUuids18(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TaskControllerApi apiInstance = new TaskControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<TaskDto> result = apiInstance.getByUuids18(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getByUuids18");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;TaskDto&gt;**](TaskDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postTasks"></a>

# **postTasks**

> List&lt;PushResult&gt; postTasks(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TaskControllerApi apiInstance = new TaskControllerApi();
List<TaskDto> body = Arrays.asList(new TaskDto()); // List<TaskDto> |
try {
    List<PushResult> result = apiInstance.postTasks(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#postTasks");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                  | Description | Notes      |
| -------- | ------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;TaskDto&gt;**](TaskDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
