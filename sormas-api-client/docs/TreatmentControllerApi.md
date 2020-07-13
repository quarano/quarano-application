# TreatmentControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                 | HTTP request                    | Description |
| ---------------------------------------------------------------------- | ------------------------------- | ----------- |
| [**getAllActiveUuids9**](TreatmentControllerApi.md#getAllActiveUuids9) | **GET** /treatments/uuids       |
| [**getAllTreatments**](TreatmentControllerApi.md#getAllTreatments)     | **GET** /treatments/all/{since} |
| [**getByUuids19**](TreatmentControllerApi.md#getByUuids19)             | **POST** /treatments/query      |
| [**postTreatments**](TreatmentControllerApi.md#postTreatments)         | **POST** /treatments/push       |

<a name="getAllActiveUuids9"></a>

# **getAllActiveUuids9**

> List&lt;String&gt; getAllActiveUuids9()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TreatmentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TreatmentControllerApi apiInstance = new TreatmentControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids9();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TreatmentControllerApi#getAllActiveUuids9");
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

<a name="getAllTreatments"></a>

# **getAllTreatments**

> List&lt;TreatmentDto&gt; getAllTreatments(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TreatmentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TreatmentControllerApi apiInstance = new TreatmentControllerApi();
Long since = 789L; // Long |
try {
    List<TreatmentDto> result = apiInstance.getAllTreatments(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TreatmentControllerApi#getAllTreatments");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;TreatmentDto&gt;**](TreatmentDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids19"></a>

# **getByUuids19**

> List&lt;TreatmentDto&gt; getByUuids19(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TreatmentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TreatmentControllerApi apiInstance = new TreatmentControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<TreatmentDto> result = apiInstance.getByUuids19(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TreatmentControllerApi#getByUuids19");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;TreatmentDto&gt;**](TreatmentDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postTreatments"></a>

# **postTreatments**

> List&lt;PushResult&gt; postTreatments(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.TreatmentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

TreatmentControllerApi apiInstance = new TreatmentControllerApi();
List<TreatmentDto> body = Arrays.asList(new TreatmentDto()); // List<TreatmentDto> |
try {
    List<PushResult> result = apiInstance.postTreatments(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TreatmentControllerApi#postTreatments");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                            | Description | Notes      |
| -------- | ----------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;TreatmentDto&gt;**](TreatmentDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
