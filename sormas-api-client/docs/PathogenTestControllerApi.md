# PathogenTestControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                          | HTTP request                           | Description |
| ------------------------------------------------------------------------------- | -------------------------------------- | ----------- |
| [**getAllActiveUuids5**](PathogenTestControllerApi.md#getAllActiveUuids5)       | **GET** /pathogentests/uuids           |
| [**getAllPathogenTests**](PathogenTestControllerApi.md#getAllPathogenTests)     | **GET** /pathogentests/all/{since}     |
| [**getBySampleUuids**](PathogenTestControllerApi.md#getBySampleUuids)           | **POST** /pathogentests/query/samples  |
| [**getByUuids12**](PathogenTestControllerApi.md#getByUuids12)                   | **POST** /pathogentests/query          |
| [**getDeletedUuidsSince3**](PathogenTestControllerApi.md#getDeletedUuidsSince3) | **GET** /pathogentests/deleted/{since} |
| [**postPathogenTests**](PathogenTestControllerApi.md#postPathogenTests)         | **POST** /pathogentests/push           |

<a name="getAllActiveUuids5"></a>

# **getAllActiveUuids5**

> List&lt;String&gt; getAllActiveUuids5()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids5();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getAllActiveUuids5");
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

<a name="getAllPathogenTests"></a>

# **getAllPathogenTests**

> List&lt;PathogenTestDto&gt; getAllPathogenTests(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
Long since = 789L; // Long |
try {
    List<PathogenTestDto> result = apiInstance.getAllPathogenTests(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getAllPathogenTests");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getBySampleUuids"></a>

# **getBySampleUuids**

> List&lt;PathogenTestDto&gt; getBySampleUuids(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<PathogenTestDto> result = apiInstance.getBySampleUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getBySampleUuids");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids12"></a>

# **getByUuids12**

> List&lt;PathogenTestDto&gt; getByUuids12(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<PathogenTestDto> result = apiInstance.getByUuids12(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getByUuids12");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince3"></a>

# **getDeletedUuidsSince3**

> List&lt;String&gt; getDeletedUuidsSince3(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
Long since = 789L; // Long |
try {
    List<String> result = apiInstance.getDeletedUuidsSince3(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#getDeletedUuidsSince3");
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

<a name="postPathogenTests"></a>

# **postPathogenTests**

> List&lt;PushResult&gt; postPathogenTests(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PathogenTestControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PathogenTestControllerApi apiInstance = new PathogenTestControllerApi();
List<PathogenTestDto> body = Arrays.asList(new PathogenTestDto()); // List<PathogenTestDto> |
try {
    List<PushResult> result = apiInstance.postPathogenTests(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PathogenTestControllerApi#postPathogenTests");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                  | Description | Notes      |
| -------- | ----------------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;PathogenTestDto&gt;**](PathogenTestDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
